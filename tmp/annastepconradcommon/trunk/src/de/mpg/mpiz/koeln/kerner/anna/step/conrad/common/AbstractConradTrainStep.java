package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAFile;
import de.bioutils.fasta.FASTAFileImpl;
import de.bioutils.fasta.FASTASequence;
import de.bioutils.gtf.GTFElement;
import de.bioutils.gtf.GTFFile;
import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.other.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;

public abstract class AbstractConradTrainStep extends AbstractConradStep {

	private final static String PROPERTIES_KEY_PREFIX = ConradConstants.PROPERTIES_KEY_PREFIX
			+ "train.";
	private final static String TRAINING_FILE_NAME_KEY = PROPERTIES_KEY_PREFIX
			+ "trainingFileName";
	
	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		try {
		super.init(context);
		logger = new LogDispatcherImpl(context);
			assignProperties();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(e);
		}
	}

	private void assignProperties() throws StepExecutionException,
			FileNotFoundException {
		final File executableDir = new File(super.getStepProperties()
				.getProperty(ConradConstants.CONRAD_DIR_KEY));
		workingDir = new File(super.getStepProperties().getProperty(
				WORKING_DIR_KEY));
		if (!FileUtils.dirCheck(workingDir, true))
			throw new FileNotFoundException("cannot access working dir "
					+ workingDir);
		final File trainingFile = new File(workingDir, super
				.getStepProperties().getProperty(TRAINING_FILE_NAME_KEY));
		process = getProcess(executableDir, workingDir, trainingFile);
	}

	@Override
	public boolean canBeSkipped(DataProxy data) throws StepExecutionException {
		try {
			final boolean trainingFile = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().exists());
			final boolean trainingFileRead = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().canRead());
			logger.debug(this, "need to run:");
			logger.debug(this, "\ttrainingFile=" + trainingFile);
			logger.debug(this, "\ttrainingFileRead=" + trainingFileRead);
			return trainingFile && trainingFileRead;
		} catch (DataBeanAccessException e) {
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			final boolean fastas = (data.getVerifiedGenesFasta() != null);
			final boolean fastasSize = (data.getVerifiedGenesFasta().size() != 0);
			final boolean gtf = (data.getVerifiedGenesGtf() != null);
			final boolean gtfSize = (data.getVerifiedGenesGtf().size() != 0);
			logger.debug(this, "requirements:");
			logger.debug(this, "\tfastas=" + fastas);
			logger.debug(this, "\tfastasSize=" + fastasSize);
			logger.debug(this, "\tgtf=" + gtf);
			logger.debug(this, "\tgtfSize=" + gtfSize);
			return (fastas && fastasSize && gtf && gtfSize);
		} catch (DataBeanAccessException e) {
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(e);
		}
	}

	protected void createFiles(DataProxy data) throws DataBeanAccessException,
			IOException {

		final File file = new File(workingDir, "ref.fasta");
		final ArrayList<? extends FASTASequence> fastas = data
				.getVerifiedGenesFasta();
		final FASTAFile fastaFile = new FASTAFileImpl(fastas);
		fastaFile.write(file);

		final File file2 = new File(workingDir, "ref.gtf");
		final ArrayList<? extends GTFElement> gtfs = data.getVerifiedGenesGtf();
		final GTFFile gtfFile = new GTFFile(gtfs);
		gtfFile.write(file2);
		
		file.deleteOnExit();
		file2.deleteOnExit();

	}
	
	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		try {
			createFiles(data);
			return process.createAndStartProcess();
		} catch (Exception e) {
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(e);
		}
	}
	
	protected abstract AbstractStepProcessBuilder getProcess(File executableDir, File workingDir, File trainingFile);
}
