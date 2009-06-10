package de.mpg.mpiz.koeln.kerner.dataproxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

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
		DataProxyActivator.LOGGER.debug(this, "got DataBean");
		return data;
	}
	
	private void writeDataBean(DataBean data) throws IOException {
		DataProxyActivator.LOGGER.debug(this, "writing DataBean " + " to " + FILE);
		de.kerner.commons.file.FileUtils.objectToFile(data, FILE);
		DataProxyActivator.LOGGER.debug(this, "writing DataBean succesfull");
	}

	public void setVerifiedGenesFasta(ArrayList<? extends FASTASequence> sequences) throws DataBeanAccessException {
		try {
		DataBean data = getDataBean();
		data.setVerifiedGenesFasta(sequences);
			writeDataBean(data);
		} catch (Exception e) {
			throw new DataBeanAccessException(e);
		}
	}

	public void setVerifiedGenesGtf(ArrayList<? extends GTFElement> elements) throws DataBeanAccessException {
		try {
			DataBean data = getDataBean();
			data.setVerifiedGenesGtf(elements);
			writeDataBean(data);
		} catch (Exception e) {
			throw new DataBeanAccessException(e);
		}
	}

	public ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws DataBeanAccessException {
		try {
			DataBean data = getDataBean();
			ArrayList<? extends FASTASequence> list;
			list = data.getVerifiedGenesFasta();
			return list;
		} catch (Exception e) {
			throw new DataBeanAccessException(e);
		}
	}

	public ArrayList<? extends GTFElement> getVerifiedGenesGtf()
			throws DataBeanAccessException {
		try{
		DataBean data = getDataBean();
		ArrayList<? extends GTFElement> list = data.getVerifiedGenesGtf();
		return list;
		} catch (Exception e) {
			throw new DataBeanAccessException(e);
		}
	}
	
	

}
