package de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import de.kerner.commons.file.LazyStringReader;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

class SerializationStrategyXML implements SerialisationStrategy {

	public DataBean readFromDisk(File file) throws DataBeanAccessException {
		try {
			return XMLToObject(DataBean.class, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataBeanAccessException(e);
		}
	}

	public void writeDataBean(DataBean dataBean, File file) throws DataBeanAccessException {
		try {
			objectToXML(dataBean, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataBeanAccessException(e);
		}

	}
	
	private static void objectToXML(Object o, File file) throws IOException {
		if (o == null || file == null)
			throw new NullPointerException();
		XStream xstream = new XStream(new DomDriver());
		FileOutputStream fs = new FileOutputStream(file);
        xstream.toXML(o, fs);
        fs.close();
	}

	private static <V> V XMLToObject(Class<V> c, File file) throws IOException {
		XStream xstream = new XStream();
		return c.cast(xstream.fromXML(new LazyStringReader(file).getString()));
	}

}
