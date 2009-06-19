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

public class DataProxyImpl implements DataProxy {

	private final File file;

	public DataProxyImpl(Server server) {
		file = new File(new File(server.getServerProperties().getProperty(
				Server.WORKING_DIR_KEY)), "dataBean.ser");
		printProperties();
	}

	private void printProperties() {
		System.out.println(this + ": created. Properties:");
		System.out.println(this + ":\tfile=" + file);

	}

	public DataBean getDataBean() throws DataBeanAccessException {
		DataBean data = null;
		synchronized (file) {
			if (file.exists()) {
				try {
					System.out.println(this + ": " + file + " exists, reading");
					data = fileToObject(DataBean.class, file);
				} catch (EOFException e) {
					System.out.println(this + ": " + file
							+ " corrupt, returning new one");
					data = new DataBeanImpl();

				} catch (StreamCorruptedException e) {
					System.out.println(this + ": " + file
							+ " corrupt, returning new one");
					data = new DataBeanImpl();
				} catch (IOException e) {
					// TODO put to log
					e.printStackTrace();
					throw new DataBeanAccessException(e);
				} catch (ClassNotFoundException e) {
					// TODO put to log
					e.printStackTrace();
					throw new DataBeanAccessException(e);
				}
			} else {
				System.out.println(this + ": " + file
						+ " does not exist, returning new one");
				data = new DataBeanImpl();
			}
			System.out.println(this + ": got DataBean");
			return data;
		}
	}

	public void updateDataBean(DataBean dataBean)
			throws DataBeanAccessException {
		// TODO current implementation just overrides existing data bean. that
		// is not so
		// good...
		synchronized (file) {
			try {
				writeDataBean(dataBean);
			} catch (IOException e) {
				throw new DataBeanAccessException(e);
			}
		}
	}

	private void writeDataBean(DataBean data) throws IOException {
		// TODO remove try catch
		try {
			System.out.println(this + ": writing DataBean" + " to " + file);
			de.kerner.commons.file.FileUtils.objectToFile(data, file);
			System.out.println(this + ": writing DataBean succesfull");
		} catch (Throwable t) {
			t.printStackTrace();
		}
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

	public String toString() {
		return this.getClass().getSimpleName();
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
