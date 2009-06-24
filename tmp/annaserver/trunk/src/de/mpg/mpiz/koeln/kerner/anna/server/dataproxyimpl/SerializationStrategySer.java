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

import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.SerialisationStrategy;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.dataimpl.DataBeanImpl;
import de.mpg.mpiz.koeln.kerner.anna.serverimpl.ServerActivator;

/**
 * 
 * @ThreadSave
 *
 */
public class SerializationStrategySer implements SerialisationStrategy {

	public synchronized DataBean readFromDisk(File file) throws DataBeanAccessException {
		try {
			final DataBean data = fileToObject(DataBean.class, file);
			ServerActivator.LOGGER.debug(this, "reading data from file");
			return data;
		} catch (EOFException e) {
			return handleCorruptData(file, e);
		} catch (StreamCorruptedException e) {
			return handleCorruptData(file, e);
		} catch (IOException e) {
			ServerActivator.LOGGER.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			ServerActivator.LOGGER.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		}
	}
	
	private DataBean handleCorruptData(File file,  Throwable t) {
		ServerActivator.LOGGER.debug(this, file.toString()
				+ " corrupt, returning new one");
		if(file.delete()){
			// all good
		} else {
			ServerActivator.LOGGER.warn(this, "could not delete corrupt databean " + file);
		}
		return new DataBeanImpl();
	}

	public synchronized void writeDataBean(DataBean dataBean, File file)
			throws DataBeanAccessException {
		try {
			ServerActivator.LOGGER.debug(this, "writing data to file");
			objectToFile(dataBean, file);
		} catch (IOException e) {
			ServerActivator.LOGGER.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		}

	}
	
	private static void objectToFile(Serializable s, File file) throws IOException {
		if (s == null || file == null)
			throw new NullPointerException(s + " + " + file
					+ " must not be null");
		OutputStream fos = new FileOutputStream(file);
		ObjectOutputStream outStream = new ObjectOutputStream(fos);
		outStream.writeObject(s);
		outStream.close();
		fos.close();
	}

	private static <V> V fileToObject(Class<V> c, File file) throws IOException,
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
	
	public String toString(){
		return this.getClass().getSimpleName()+"@"+Integer.toHexString(hashCode());
	}

}
