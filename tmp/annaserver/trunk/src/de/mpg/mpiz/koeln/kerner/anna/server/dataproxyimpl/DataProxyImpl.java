package de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
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
public class DataProxyImpl implements DataProxy {

	private final File file;
	private final SerialisationStrategy strategy;

	public DataProxyImpl(Server server, SerialisationStrategy strategy) {
		file = new File(new File(server.getServerProperties().getProperty(
				Server.WORKING_DIR_KEY)), "dataBean.ser");
		this.strategy = strategy;
		printProperties();
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
	
	private DataBean getDataBean() throws DataBeanAccessException{
		if(file.exists() && file.canRead()){
			return strategy.readFromDisk(file);
		} else {
			return new DataBeanImpl();
		}
	}

	public synchronized ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getVerifiedGenesFasta();
	}

	public synchronized ArrayList<? extends GTFElement> getVerifiedGenesGtf()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getVerifiedGenesGtf();
	}

	public synchronized void setVerifiedGenesFasta(
			ArrayList<? extends FASTASequence> sequences)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setVerifiedGenesFasta(sequences);
		strategy.writeDataBean(data, file);
	}

	public synchronized void setVerifiedGenesGtf(ArrayList<? extends GTFElement> elements)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setVerifiedGenesGtf(elements);
		strategy.writeDataBean(data, file);
	}

	public synchronized ArrayList<? extends FASTASequence> getInputSequences()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getInputSequences();
	}

	public synchronized void setInputSequences(ArrayList<? extends FASTASequence> fastas)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setInputSequences(fastas);
		strategy.writeDataBean(data, file);
	}

	public synchronized File getConradTrainingFile() throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getConradTrainingFile();
	}

	public synchronized void setConradTrainingFile(File trainingFile)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setConradTrainingFile(trainingFile);
		strategy.writeDataBean(data, file);
	}

	public synchronized ArrayList<? extends GTFElement> getPredictedGenesGtf()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getPredictedGenesGtf();
	}

	public synchronized void setPredictedGenesGtf(ArrayList<? extends GTFElement> result)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setPredictedGenesGtf(result);
		strategy.writeDataBean(data, file);
		
	}

}
