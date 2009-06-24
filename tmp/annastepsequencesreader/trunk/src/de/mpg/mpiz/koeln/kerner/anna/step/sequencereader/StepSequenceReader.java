package de.mpg.mpiz.koeln.kerner.anna.step.sequencereader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxyProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

public class StepSequenceReader extends AbstractStep {

	// TODO that refers to training step
	private final static String FASTA_KEY = "anna.step.conrad.train.fasta";
	// TODO that refers to training step
	private final static String GTF_KEY = "anna.step.conrad.train.gtf";

	// not final, due to delayed initiation
	private File fasta;
	private File gtf;
	private LogDispatcher logger = null;

	public StepSequenceReader() {
		// use "init()" instead, to make sure "logger" is initiated
	}

	@Override
	protected void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		logger = new LogDispatcherImpl(context);
		initFiles();
	}

	private void initFiles() {
		final String fastaPath = super.getStepProperties().getProperty(
				FASTA_KEY);
		logger.debug(this, "got path for FASTA: " + fastaPath);
		final String gtfPath = super.getStepProperties().getProperty(GTF_KEY);
		logger.debug(this, "got path for GTF: " + gtfPath);
		fasta = new File(fastaPath);
		gtf = new File(gtfPath);
	}

	@Override
	public boolean requirementsSatisfied(DataProxyProvider data) {
		logger.info(this, ": no requirements needed");
		return true;
	}

	@Override
	public boolean run(DataProxyProvider data, StepProcessObserver observer){
		observer.setProgress(0, 100);
		try {
			observer.setProgress(30, 100);
			doFasta(data);
			observer.setProgress(60, 100);
			doGtf(data);
			observer.setProgress(100, 100);
		} catch (DataBeanAccessException e) {
			logger.error(this, e.toString(), e);
			return false;
		} catch (IOException e) {
			logger.error(this, e.toString(), e);
			return false;
		} catch (GTFFormatErrorException e) {
			logger.error(this, e.toString(), e);
			return false;
		}
		return true;
	}

	private void doGtf(DataProxyProvider data) throws IOException,
			GTFFormatErrorException, DataBeanAccessException {
			logger.info(this, "reading GTF file " + gtf);
			final GTFFile gtfFile = new GTFFile(gtf);
			final ArrayList<? extends GTFElement> elements = gtfFile
					.getElements();
			logger.info(this, "done reading gtf");
			data.getDataProxy().setVerifiedGenesGtf(elements);
	}

	private void doFasta(DataProxyProvider data) throws IOException,
			DataBeanAccessException {
		try{
		logger.info(this, "reading FASTA file " + fasta);
			final FASTAFile fastaFile = new FASTAFile(fasta);
			final ArrayList<? extends FASTASequence> sequences = fastaFile
					.getSequences();
			logger.info(this, "done reading fasta");
			data.getDataProxy().setVerifiedGenesFasta(sequences);
		}catch(Throwable t){
			t.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public boolean canBeSkipped(DataProxyProvider data) throws StepExecutionException{
		try {
			// TODO size == 0 sub-optimal indicator
			final ArrayList<? extends FASTASequence> list1 = data.getDataProxy()
					.getVerifiedGenesFasta();
			final ArrayList<? extends GTFElement> list2 = data.getDataProxy()
					.getVerifiedGenesGtf();
			return (list1 != null && list1.size() != 0 && list2 != null && list2
					.size() != 0);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}
	
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
