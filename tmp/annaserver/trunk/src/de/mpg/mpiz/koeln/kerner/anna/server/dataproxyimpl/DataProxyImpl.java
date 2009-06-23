package de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

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

	public DataProxyImpl(Server server) {
		file = new File(new File(server.getServerProperties().getProperty(
				Server.WORKING_DIR_KEY)), "dataBean.ser");
		printProperties();
	}

	public void updateDataBean(DataBean dataBean)
			throws DataBeanAccessException {
		// TODO current implementation just overrides existing data bean. that
		// is not so
		// good...

		try {
			synchronized (file) {
				writeDataBean(dataBean);
			}
		} catch (IOException e) {
			ServerActivator.LOGGER.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		}

	}

	public DataBean getDataBean() throws DataBeanAccessException {
		synchronized (file) {
			DataBean data = null;
			if (file.exists()) {
				data = readFromDisk();
			} else {
				ServerActivator.LOGGER.debug(this, file
						+ " does not exist, returning new one");
				data = DataBeanImpl.INSTANCE;
			}
			return data;
		}
	}

	private DataBean readFromDisk() throws DataBeanAccessException {
		try {
			ServerActivator.LOGGER.debug(this, file + " exists, reading");
			return fileToObject(DataBean.class, file);
		} catch (EOFException e) {
			return handleCorruptData(e);
		} catch (StreamCorruptedException e) {
			return handleCorruptData(e);
		} catch (IOException e) {
			ServerActivator.LOGGER.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			ServerActivator.LOGGER.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		}
	}

	private DataBean handleCorruptData(Throwable t) {
		ServerActivator.LOGGER.debug(this, file.toString()
				+ " corrupt, returning new one", t);
		file.delete();
		return DataBeanImpl.INSTANCE;
	}

	private void writeDataBean(DataBean data) throws IOException {
		ServerActivator.LOGGER.debug(this, "writing DataBean" + " to " + file);
		de.kerner.commons.file.FileUtils.objectToFile(data, file);
		ServerActivator.LOGGER.debug(this, "writing DataBean succesfull");
	}

	private void printProperties() {
		ServerActivator.LOGGER.debug(this, "created. Properties:\n\tfile="
				+ file);
		ServerActivator.LOGGER.debug(this, ":\tfile=" + file);
	}

	public String toString() {
		return this.getClass().getSimpleName()+Integer.toHexString(this.hashCode());
	}

	static void objectToFile(Serializable s, File file) throws IOException {
		if (s == null || file == null)
			throw new NullPointerException(s + " + " + file
					+ " must not be null");
		OutputStream fos = new FileOutputStream(file);
		ObjectOutputStream outStream = new ObjectOutputStream(fos);
		outStream.writeObject(s);
		outStream.close();
		fos.close();
	}

	static <V> V fileToObject(Class<V> c, File file) throws IOException,
			ClassNotFoundException {
		if (c == null || file == null)
			throw new NullPointerException(c + " + " + file
					+ " must not be null");
		InputStream fis = new FileInputStream(file);
		ObjectInputStream inStream = new ObjectInputStream(fis);
		V v = c.cast(inStream.readObject());
		inStream.close();
		fis.close();
		return v;
	}
}
