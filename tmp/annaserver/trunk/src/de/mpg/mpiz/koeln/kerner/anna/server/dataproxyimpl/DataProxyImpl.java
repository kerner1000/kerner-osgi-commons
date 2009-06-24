package de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl;

import java.io.File;

import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.dataimpl.DataBeanImpl;
import de.mpg.mpiz.koeln.kerner.anna.serverimpl.ServerActivator;

/**
 * 
 * @ThreadSave
 * 
 */
public class DataProxyImpl implements DataProxy {

	private final File file;
	private final SerialisationStrategy strategy;

	public DataProxyImpl(Server server, SerialisationStrategy strategy) {
		file = new File(new File(server.getServerProperties().getProperty(
				Server.WORKING_DIR_KEY)), "dataBean.ser");
		this.strategy = strategy;
		printProperties();
	}

	public void updateDataBean(DataBean dataBean)
			throws DataBeanAccessException {
		synchronized (file) {
			// TODO try catch
			try {
				ServerActivator.LOGGER.debug(this, "updating dataBean:");
				final DataBean currentDataBean = getDataBean();
				ServerActivator.LOGGER.debug(this, "current bean="+currentDataBean);
				final DataBean mergedDataBean = mergeDataBeans(currentDataBean, dataBean);
				ServerActivator.LOGGER.debug(this, "merged bean="+mergedDataBean);
				strategy.writeDataBean(mergedDataBean, file);
			} catch (Throwable t) {
				t.printStackTrace();
				System.exit(1);
			}
		}
	}

	private DataBean mergeDataBeans(DataBean currentDataBean, DataBean newDataBean) throws DataBeanAccessException {
		// TODO
		return newDataBean;
	}

	public DataBean getDataBean() throws DataBeanAccessException {
		synchronized (file) {
			DataBean data = null;
			if (file.exists()) {
				// TODO try catch
				try {
					final DataBean tmp = strategy.readFromDisk(file);
					ServerActivator.LOGGER.debug(this, "reading dataBean precheck: " + tmp.toString());
					ServerActivator.LOGGER.debug(this, "\tFile:" + tmp.getConradTrainingFile());
					data = strategy.readFromDisk(file);
					ServerActivator.LOGGER.debug(this, "dataBean read: " + data.toString());
					ServerActivator.LOGGER.debug(this, "\tFile:" + data.getConradTrainingFile());
				} catch (Throwable t) {
					t.printStackTrace();
					System.exit(1);
				}
			} else {
				ServerActivator.LOGGER.debug(this, file
						+ " does not exist, returning new one");
				data = new DataBeanImpl();
			}
			return data;
		}
	}

	private void printProperties() {
		ServerActivator.LOGGER.debug(this, "created. Properties:\n\tfile="
				+ file);
		ServerActivator.LOGGER.debug(this, ":\tfile=" + file);
	}

	public String toString() {
		return this.getClass().getSimpleName()
				+ Integer.toHexString(this.hashCode());
	}
}
