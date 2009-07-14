package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.abstractstep.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

public class StepConradTrain extends AbstractStep {

	private final static String PROPERTIES_KEY_PREFIX = ConradConstants.PROPERTIES_KEY_PREFIX
			+ "train.";
	private final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "workingDir";
	private final static String TRAINING_FILE_NAME_KEY = PROPERTIES_KEY_PREFIX
			+ "trainingFileName";
	private final static String FASTA_KEY = PROPERTIES_KEY_PREFIX + "fasta";
	private final static String GTF_KEY = PROPERTIES_KEY_PREFIX + "gtf";
	private final static String RUN_KEY = PROPERTIES_KEY_PREFIX + "run";
	private final static String RUN_VALUE_LOCAL = "local";
	private final static String RUN_VALUE_LSF = "lsf";
	private final static String DEFAULT_RUN_VALUE = RUN_VALUE_LOCAL;

	private AbstractRunStateTraining state;
	private File conradWorkingDir, stepWorkingDir, fastaFile, gtfFile;
	private File trainingFile;
	private String runEnv;
	private LogDispatcher logger;

	public StepConradTrain() {
	}

	@Override
	protected synchronized void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		logger = new LogDispatcherImpl(context);
		assignProperties();
		validateProperties();
		printProperties();

	}

	private void assignProperties() {
		conradWorkingDir = new File(super.getStepProperties().getProperty(
				ConradConstants.CONRAD_DIR_KEY));
		stepWorkingDir = new File(super.getStepProperties().getProperty(
				WORKING_DIR_KEY));
		trainingFile = new File(stepWorkingDir, super.getStepProperties()
				.getProperty(TRAINING_FILE_NAME_KEY));
		fastaFile = new File(super.getStepProperties().getProperty(FASTA_KEY));
		gtfFile = new File(super.getStepProperties().getProperty(GTF_KEY));
		runEnv = super.getStepProperties().getProperty(RUN_KEY,
				DEFAULT_RUN_VALUE);
	}

	private void validateProperties() throws StepExecutionException {
		if (!conradWorkingDir.exists() || !conradWorkingDir.canRead())
			throw new StepExecutionException("cannot access conrad working dir");
		if (!checkWorkingDir(stepWorkingDir))
			throw new StepExecutionException("cannot access step working dir");
		if (!fastaFile.exists() || !fastaFile.canRead())
			throw new StepExecutionException("cannot access fasta file "
					+ fastaFile);
		if (!gtfFile.exists() || !gtfFile.canRead())
			throw new StepExecutionException("cannot access gtf file "
					+ gtfFile);
	}

	private void printProperties() {
		logger.debug(this, " created, properties:");
		logger.debug(this, "\tstepWorkingDir=" + stepWorkingDir);
		logger.debug(this, "\tconradWorkingDir=" + conradWorkingDir);
		logger.debug(this, "\tfastaFile=" + fastaFile);
		logger.debug(this, "\tgtfFile=" + gtfFile);
		logger.debug(this, "\ttraiingFile=" + trainingFile);
		logger.debug(this, "\trunEnv=" + runEnv);
	}

	private boolean checkWorkingDir(final File workingDir) {
		if (!workingDir.exists()) {
			System.out.println(this + ": " + workingDir
					+ " does not exist, creating");
			final boolean b = workingDir.mkdirs();
			return b;
		}
		return workingDir.canWrite();
	}

	@Override
	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			final boolean fastas = (data.getVerifiedGenesFasta() != null);
			final boolean fastasSize = (data
					.getVerifiedGenesFasta().size() != 0);
			final boolean gtf = (data.getVerifiedGenesGtf() != null);
			final boolean gtfSize = (data.getVerifiedGenesGtf()
					.size() != 0);
			logger.debug(this, "requirements:");
			logger.debug(this, "\tfastas=" + fastas);
			logger.debug(this, "\tfastasSize=" + fastasSize);
			logger.debug(this, "\tgtf=" + gtf);
			logger.debug(this, "\tgtfSize=" + gtfSize);
			return (fastas && fastasSize && gtf && gtfSize);
		} catch (DataBeanAccessException e) {
			logger.error(this, e.toString(), e);
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean canBeSkipped(DataProxy data)
			throws StepExecutionException {
		try {
			final boolean trainingFile = (data
					.getConradTrainingFile() != null && data
					.getConradTrainingFile().exists());
			final boolean trainingFileRead = (data
					.getConradTrainingFile() != null && data
					.getConradTrainingFile().canRead());
			logger.debug(this, "need to run:");
			logger.debug(this, "\ttrainingFile=" + trainingFile);
			logger.debug(this, "\ttrainingFileRead=" + trainingFileRead);
			return trainingFile && trainingFileRead;
		} catch (Throwable t) {
			logger.error(this, t.toString(), t);
			t.printStackTrace();
			System.exit(1);
			throw new StepExecutionException(t);
		}
	}

	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		boolean success = true;
		try {
			if (trainingFile.exists()) {
				takeShortcut(data);
				return success;
			}
			assignState();
			final ArrayList<? extends FASTASequence> fastas = data
					.getVerifiedGenesFasta();
			final ArrayList<? extends GTFElement> elements = data
					.getVerifiedGenesGtf();
			success = state.run(fastas, elements);
			if (success) {
				logger.info(this, "training sucessfull");
				data.setConradTrainingFile(state.getResult());
				return success;
			}
			logger.warn(this, "training not sucessfull, will not update data");
			return false;
		} catch (DataBeanAccessException e) {
			logger.error(this, e.toString(), e);
			return false;
		}
	}

	private void takeShortcut(DataProxy data)
			throws DataBeanAccessException {
		logger.info(this, "training file already exists, taking short cut ("
				+ trainingFile + ")");
		logger.info(this, "training sucessfull");
		data.setConradTrainingFile(trainingFile);
	}

	private void assignState() {
		if (runEnv.equalsIgnoreCase(RUN_VALUE_LSF)) {
			runOnLSF();
		} else if (runEnv.equalsIgnoreCase(RUN_VALUE_LOCAL)) {
			runLocally();

		} else {
			logger.warn(this, "unrecognized running env \""
					+ "\", going to run locally");
			runLocally();
		}
	}

	private void runLocally() {
		logger.info(this, "going to run locally");
		state = new RunStateLocal(conradWorkingDir, stepWorkingDir,
				trainingFile, fastaFile, gtfFile, logger);
	}

	private void runOnLSF() {
		logger.info(this, "going to run on LSF");
		state = new RunStateLSF(conradWorkingDir, stepWorkingDir, trainingFile,
				fastaFile, gtfFile, logger);
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
