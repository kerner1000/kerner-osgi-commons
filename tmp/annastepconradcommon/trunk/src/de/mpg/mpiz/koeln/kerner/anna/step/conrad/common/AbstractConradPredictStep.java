package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gtf.GTFElement;
import de.bioutils.gtf.GTFFile;
import de.kerner.commons.file.FileUtils;
import de.kerner.commons.file.LazyFileCopier;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.other.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;

public abstract class AbstractConradPredictStep extends AbstractConradStep {
	
	private File trainingFile;
	private File resultFile;

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
		trainingFile = new File(workingDir, "trainingFile.bin");
		resultFile = new File(workingDir, "result.gtf");
		process = getProcess(executableDir, workingDir, trainingFile, resultFile);
	}

	@Override
	public boolean canBeSkipped(DataProxy data) throws StepExecutionException {

		// TODO size may be zero, if nothing was found
		try {
			final boolean predictedGtf = (data.getPredictedGenesGtf() != null);
			final boolean predictedGtfSize = (data.getPredictedGenesGtf()
					.size() != 0);
			logger.debug(this, "need to run:");
			logger.debug(this, "\tpredictedGtf=" + predictedGtf);
			logger.debug(this, "\tpredictedGtfSize=" + predictedGtfSize);
			return (predictedGtf && predictedGtfSize);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			final boolean trainingFile = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().exists());
			final boolean trainingFileRead = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().canRead());
			final boolean inputSequences = (data.getInputSequences() != null);
			final boolean inputSequencesSize = (data.getInputSequences().size() != 0);
			logger.debug(this, "requirements:");
			logger.debug(this, "\ttrainingFile=" + trainingFile);
			logger.debug(this, "\ttrainingFileRead=" + trainingFileRead);
			logger.debug(this, "\tinputSequences=" + inputSequences);
			logger.debug(this, "\tinputSequencesSize=" + inputSequencesSize);
			return (trainingFile && trainingFileRead && inputSequences && inputSequencesSize);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		boolean success = true;
		try {
			createFiles(data);
			process.addResultFile(true, resultFile);
			success = process.createAndStartProcess();
			if(success)
				update(resultFile, data);
		} catch (Exception e) {
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(e);
		}
		return success;
	}

	private void update(File resultFile, DataProxy data) throws IOException, GFFFormatErrorException, DataBeanAccessException {
		final Collection<? extends GTFElement> c = new GTFFile(resultFile, null).getElements();
		data.setPredictedGenesGtf(new ArrayList<GTFElement>(c));	
	}

	private void createFiles(DataProxy data) throws DataBeanAccessException,
			IOException {

		final File file = new File(workingDir, "ref.fasta");
		new FASTAFileImpl(data.getInputSequences()).write(file);

		final File file2 = data.getConradTrainingFile();
		new LazyFileCopier(file2, trainingFile).copy();

		trainingFile.deleteOnExit();
		file.deleteOnExit();
	}

	protected abstract AbstractStepProcessBuilder getProcess(File executableDir, File workingDir, File trainingFile, File resultFile);
}
