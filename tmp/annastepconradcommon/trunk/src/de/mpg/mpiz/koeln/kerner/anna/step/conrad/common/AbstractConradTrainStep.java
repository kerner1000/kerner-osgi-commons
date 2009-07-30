package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAFile;
import de.bioutils.fasta.FASTAFileImpl;
import de.bioutils.fasta.FASTASequence;
import de.bioutils.gtf.GTFElement;
import de.bioutils.gtf.GTFFile;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepUtils;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 * 
 */
public abstract class AbstractConradTrainStep extends AbstractConradStep {

	protected File inFasta;
	protected File inGff;
	protected File trainingFile;

	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		try {
			super.init(context);
			logger.debug(this, "doing initialisation");
			trainingFile = new File(workingDir, "trainingFile.bin");
			logger.debug(this, "init done: workingDir=" + workingDir.getAbsolutePath());
			logger.debug(this, "init done: trainingFile="
					+ trainingFile.getAbsolutePath());
			logger.debug(this, "init done: process=" + process);
		} catch (Exception e) {
			StepUtils.handleStepException(this, e, logger);
		}
		logger.debug(this, "initialisation done");
	}

	@Override
	public boolean canBeSkipped(DataProxy data) throws StepExecutionException {
		try {
			final boolean trainingFile = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().exists());
			
			final boolean trainingFileRead = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().canRead());
			
			logger.debug(this, "need to run: trainingFile=" + trainingFile);
			logger.debug(this, "need to run: trainingFileRead="+ trainingFileRead);
			
			return trainingFile && trainingFileRead;
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
			final boolean fastas = (data.getVerifiedGenesFasta() != null);
			final boolean fastasSize = (data.getVerifiedGenesFasta().size() != 0);
			final boolean gtf = (data.getVerifiedGenesGtf() != null);
			final boolean gtfSize = (data.getVerifiedGenesGtf().size() != 0);
			logger.debug(this, "requirements: fastas=" + fastas);
			logger.debug(this, "requirements: fastasSize=" + fastasSize);
			logger.debug(this, "requirements: gtf=" + gtf);
			logger.debug(this, "requirements: gtfSize=" + gtfSize);
			return (fastas && fastasSize && gtf && gtfSize);
		} catch (Exception e) {
			StepUtils.handleStepException(this, e, logger);
			// cannot be reached
			return false;
		}
	}

	private void createFiles(DataProxy data) throws DataBeanAccessException,
			IOException {
		inFasta = new File(workingDir, "ref.fasta");
		logger.debug(this, "ref.fasta=" + inFasta);
		
		logger.debug(this, "getting fastas for veryfied genes");
		final ArrayList<? extends FASTASequence> fastas = data
				.getVerifiedGenesFasta();
		
		final FASTAFile fastaFile = new FASTAFileImpl(fastas);
		
		logger.debug(this, "writing fastas to " + inFasta);
		fastaFile.write(inFasta);
		
		final File inGff = new File(workingDir, "ref.gtf");
		logger.debug(this, "ref.gtf=" + inGff);
		
		logger.debug(this, "getting gtfs for veryfied genes");
		final ArrayList<? extends GTFElement> gtfs = data.getVerifiedGenesGtf();
		
		final GTFFile gtfFile = new GTFFile(gtfs);
		
		logger.debug(this, "writing gtfs to " + inGff);
		gtfFile.write(inGff);
		
		inFasta.deleteOnExit();
		inGff.deleteOnExit();
	}

	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		logger.debug(this, "running");
		boolean success = true;
		try {
			logger.debug(this, "creating ref.* files");
			createFiles(data);
			logger.debug(this, "starting process");
			process.addResultFile(true, trainingFile.getAbsoluteFile());
			success = process.createAndStartProcess();
			if (success) {
				logger.debug(this, "process sucessfull, updating data bean");
				update(data);
			}
		} catch (Exception e) {
			StepUtils.handleStepException(this, e, logger);
			// cannot be reached
			return false;
		}
		logger.debug(this, "process sucessfull=" + success);
		return success;
	}

	protected void update(DataProxy data) throws DataBeanAccessException {
		data.setConradTrainingFile(trainingFile.getAbsoluteFile());
	}
}
