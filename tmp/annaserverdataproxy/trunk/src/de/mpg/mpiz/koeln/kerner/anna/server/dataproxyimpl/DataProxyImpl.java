package de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.dataimpl.DataBeanImpl;

public class DataProxyImpl implements DataProxy {

	private final static File FILE = new File("/home/pcb/kerner/Desktop/",
			"dataBean.ser");

	public DataBean getDataBean() throws DataBeanAccessException {
		DataBean data = null;
		synchronized (FILE) {
			if (FILE.exists()) {
				try {
					System.out.println(this + ": " + FILE + " exists, reading");
					data = fileToObject(
							DataBean.class, FILE);
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
				System.out.println(this + ": " + FILE
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
		synchronized (FILE) {
			try {
				writeDataBean(dataBean);
			} catch (IOException e) {
				throw new DataBeanAccessException(e);
			}
		}
	}

	private void writeDataBean(DataBean data) throws IOException {
		System.out.println(this + ": writing DataBean" + " to " + FILE);
		de.kerner.commons.file.FileUtils.objectToFile(data, FILE);
		System.out.println(this + ": writing DataBean succesfull");
	}

	static void objectToFile(Serializable s, File file)
			throws IOException {
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
