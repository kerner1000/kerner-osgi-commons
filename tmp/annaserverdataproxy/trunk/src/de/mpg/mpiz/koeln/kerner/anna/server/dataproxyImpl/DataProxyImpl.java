package de.mpg.mpiz.koeln.kerner.anna.server.dataproxyImpl;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataImpl.DataBeanImpl;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;

/**
 * 
 * @ThreadSave (everything protected by this)
 * 
 */
public class DataProxyImpl implements DataProxy {

	private final File file;
	private final SerialisationStrategy strategy;
	private final LogDispatcher logger;
	
	public DataProxyImpl(SerialisationStrategy strategy, LogDispatcher logger) {
		// TODO File hard coded !!
		this.file = new File(
				"/home/pcb/kerner/Dropbox/mpiz/diplom/pipelinetest/serverDir/dataBean.ser");
		this.strategy = strategy;
		this.logger = logger;
		printProperties();
	}

	public DataProxyImpl(SerialisationStrategy strategy) {
		// TODO File hard coded !!
		this.file = new File(
				"/home/pcb/kerner/Dropbox/mpiz/diplom/pipelinetest/serverDir/dataBean.ser");
		this.strategy = strategy;
		this.logger = null;
		printProperties();
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

	public synchronized void setVerifiedGenesGtf(
			ArrayList<? extends GTFElement> elements)
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

	public synchronized void setInputSequences(
			ArrayList<? extends FASTASequence> fastas)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setInputSequences(fastas);
		strategy.writeDataBean(data, file);
	}

	public synchronized File getConradTrainingFile()
			throws DataBeanAccessException {
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

	public synchronized void setPredictedGenesGtf(
			ArrayList<? extends GTFElement> result)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setPredictedGenesGtf(result);
		strategy.writeDataBean(data, file);
	}

	public synchronized ArrayList<? extends GTFElement> getRepeatMaskerGtf()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getRepeatMaskerGtf();
	}

	public synchronized void setRepeatMaskerGtf(
			ArrayList<? extends GTFElement> elements)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setRepeatMaskerGtf(elements);
		strategy.writeDataBean(data, file);
	}
	
	public String toString() {
		return this.getClass().getSimpleName()
				+ Integer.toHexString(this.hashCode());
	}
	
	private void printProperties() {
		logger.debug(this, "created. Properties:\n\tfile=" + file);
		logger.debug(this, "\tfile=" + file);
	}

	private DataBean getDataBean() throws DataBeanAccessException {
		if (file.exists() && file.canRead()) {
			return strategy.readFromDisk(file);
		} else {
			logger.debug(this, "cannot read databean " + file + ", creating new databean");
			return new DataBeanImpl();
		}
	}
}
