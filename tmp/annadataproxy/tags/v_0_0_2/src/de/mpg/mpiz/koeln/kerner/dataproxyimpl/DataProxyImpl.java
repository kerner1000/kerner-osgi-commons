package de.mpg.mpiz.koeln.kerner.dataproxyimpl;

import java.io.File;
import java.io.IOException;

import de.mpg.mpiz.koeln.kerner.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.dataproxy.dataimpl.DataBeanImpl;

public class DataProxyImpl implements DataProxy {

	private final static File FILE = new File("/home/pcb/kerner/Desktop/",
			"dataBean.ser");

	public DataBeanImpl getDataBean() throws DataBeanAccessException {
		DataBeanImpl data = null;
		synchronized (FILE) {
			if (FILE.exists()) {
				try {
					System.out.println(this + ": " + FILE + " exists, reading");
					data = de.kerner.commons.file.FileUtils.fileToObject(
							DataBeanImpl.class, FILE);
				} catch (IOException e) {
					throw new DataBeanAccessException(e);
				} catch (ClassNotFoundException e) {
					throw new DataBeanAccessException(e);
				}
			} else {
				System.out.println(this + ": " + FILE
						+ " does not exist, creating");
				data = new DataBeanImpl();
			}
			System.out.println(this + ": got DataBean");
			return data;
		}
	}

	public void updateDataBean(DataBeanImpl dataBean)
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

	private void writeDataBean(DataBeanImpl data) throws IOException {
		System.out.println(this + ": writing DataBean " + " to " + FILE);
		de.kerner.commons.file.FileUtils.objectToFile(data, FILE);
		System.out.println(this + ": writing DataBean succesfull");
	}
}
