package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gtf.GTFElement;
import de.bioutils.gtf.GTFFile;
import de.kerner.commons.file.LazyFileCopier;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepUtils;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 *
 */
public abstract class AbstractConradPredictStep extends AbstractConradStep {
	
	protected File trainingFile;
	protected File resultFile;

	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		try {
			// oder important
			trainingFile = new File(workingDir, "trainingFile.bin");
			resultFile = new File(workingDir, "result.gtf");
			logger = new LogDispatcherImpl(context);
			super.init(context);
			super.init();
			//
			
			logger.debug(this, "init done: workingDir=" + workingDir.getAbsolutePath());
			logger.debug(this, "init done: trainingFile="
					+ trainingFile.getAbsolutePath());
			logger.debug(this, "init done: process=" + process);
		} catch (Exception e) {
			StepUtils.handleStepException(this, e, logger);
		}
	}
	
	@Override
	public boolean canBeSkipped(DataProxy data) throws StepExecutionException {

		// TODO size may be zero, if nothing was found
		try {
			final boolean predictedGtf = (data.getPredictedGenesGtf() != null);
			final boolean predictedGtfSize = (data.getPredictedGenesGtf()
					.size() != 0);
			logger.debug(this, "need to run: predictedGtf=" + predictedGtf);
			logger.debug(this, "need to run: predictedGtfSize=" + predictedGtfSize);
			return (predictedGtf && predictedGtfSize);
		} catch (Exception e) {
			StepUtils.handleStepException(this, e, logger);
			// cannot be reached
			return false;
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
			
			logger.debug(this, "requirements: trainingFile=" + trainingFile);
			logger.debug(this, "requirements: trainingFileRead=" + trainingFileRead);
			logger.debug(this, "requirements: inputSequences=" + inputSequences);
			logger.debug(this, "requirements: inputSequencesSize=" + inputSequencesSize);
			
			return (trainingFile && trainingFileRead && inputSequences && inputSequencesSize);
		} catch (Exception e) {
			StepUtils.handleStepException(this, e, logger);
			// cannot be reached
			return false;
		}
	}

	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		boolean success = true;
		try {
			createFiles(data);
			process.addResultFile(true, resultFile.getAbsoluteFile());
			success = process.createAndStartProcess();
			if(success)
				update(resultFile.getAbsoluteFile(), data);
		} catch (Exception e) {
			StepUtils.handleStepException(this, e, logger);
			// cannot be reached
			return false;
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
}
