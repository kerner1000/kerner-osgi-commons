package de.mpg.mpiz.koeln.kerner.dataproxy;

import java.io.File;
import java.io.IOException;

class DataProxyImpl implements DataProxy {

	private final static File FILE = new File("/home/pcb/kerner/Desktop/",
			"dataBean.ser");

	public DataBean getDataBean() throws DataBeanAccessException {
		DataBean data = null;
		synchronized (FILE) {
			if (FILE.exists()) {
				try {
					DataProxyActivator.LOGGER.debug(this, FILE
							+ " exists, reading");
					data = de.kerner.commons.file.FileUtils.fileToObject(
							DataBean.class, FILE);
				} catch (IOException e) {
					throw new DataBeanAccessException(e);
				} catch (ClassNotFoundException e) {
					throw new DataBeanAccessException(e);
				}
			} else {
				DataProxyActivator.LOGGER.debug(this, FILE
						+ " does not exist, creating");
				data = new DataBean();
			}
			DataProxyActivator.LOGGER.debug(this, "got DataBean");
			return data;
		}
	}
	
	/**
	 * current implementation just overrides existing data bean.
	 * that is not so good...
	 * @throws DataBeanAccessException 
	 */
	public void updateDataBean(DataBean dataBean) throws DataBeanAccessException {
		synchronized (FILE) {
			try {
				writeDataBean(dataBean);
			} catch (IOException e) {
				throw new DataBeanAccessException(e);
			}
		}
	}

	private void writeDataBean(DataBean data) throws IOException {
		DataProxyActivator.LOGGER.debug(this, "writing DataBean " + " to "
				+ FILE);
		de.kerner.commons.file.FileUtils.objectToFile(data, FILE);
		DataProxyActivator.LOGGER.debug(this, "writing DataBean succesfull");
	}
}
