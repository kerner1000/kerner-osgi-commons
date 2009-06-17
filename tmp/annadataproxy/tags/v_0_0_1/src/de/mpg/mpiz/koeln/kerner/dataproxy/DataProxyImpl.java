package de.mpg.mpiz.koeln.kerner.dataproxy;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.kerner.commons.file.FileUtils;

class DataProxyImpl implements DataProxy {

	private final static File FILE = new File(
			"/home/pcb/kerner/Desktop/", "dataBean.ser");

	private DataBean getDataBean() throws DataBeanAccessException {
		DataBean data = null;
		if (FILE.exists()) {
			try {
				DataProxyActivator.LOGGER.debug(this, FILE + " exists, reading");
				data = de.kerner.commons.file.FileUtils.fileToObject(
						DataBean.class, FILE);
			} catch (IOException e) {
				throw new DataBeanAccessException(e);
			} catch (ClassNotFoundException e) {
				throw new DataBeanAccessException(e);
			}
		} else {
			DataProxyActivator.LOGGER.debug(this, FILE + " does not exist, creating");
			data = new DataBean();
		}
		DataProxyActivator.LOGGER.debug(this, "got DataBean " + data);
		return data;
	}
	
	private void writeDataBean(DataBean data) throws IOException {
		DataProxyActivator.LOGGER.debug(this, "writing DataBean " + " to " + FILE + ", DataBean: " + data);
		de.kerner.commons.file.FileUtils.objectToFile(data, FILE);
		DataProxyActivator.LOGGER.debug(this, "writing DataBean succesfull");
	}

	@SuppressWarnings("unchecked")
	public void setVerifiedGenesFasta(ArrayList<? extends FASTASequence> sequences) throws DataBeanAccessException {
		try {
		ArrayList<FASTASequence> newList = Utils.deepCopy(ArrayList.class, sequences);
		DataBean data = getDataBean();
		data.addVerifiedGenesFasta(newList);
			writeDataBean(data);
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void setVerifiedGenesGtf(ArrayList<? extends GTFElement> el) throws DataBeanAccessException {
		try {
			ArrayList<GTFElement> newList = Utils.deepCopy(ArrayList.class, el);
			DataBean data = getDataBean();
			data.addVerifiedGenesGtf(newList);
			writeDataBean(data);
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws DataBeanAccessException {
		DataBean data = getDataBean();
		ArrayList<? extends FASTASequence> list = data.getVerifiedGenesFasta();
		return list;
	}

	public ArrayList<? extends GTFElement> getVerifiedGenesGtf()
			throws DataBeanAccessException {
		DataBean data = getDataBean();
		ArrayList<? extends GTFElement> list = data.getVerifiedGenesGtf();
		return list;
	}
	
	

}
