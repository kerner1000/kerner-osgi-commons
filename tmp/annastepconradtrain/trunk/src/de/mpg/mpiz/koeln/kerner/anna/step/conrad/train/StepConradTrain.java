package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;
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
	protected void init(BundleContext context) throws StepExecutionException {
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
	public boolean requirementsSatisfied(DataBean data)
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
			logger.error(this, e.toString(), e);
			throw new StepExecutionException(e);
		}

	}

	@Override
	public boolean needToRun(DataBean data) throws StepExecutionException {
		try {
			final boolean trainingFile = (!data.getConradTrainingFile().exists());
			final boolean trainingFileRead = (!data.getConradTrainingFile().canRead());
			logger.debug(this, "need to run:");
			logger.debug(this, "\ttrainingFile=" + trainingFile);
			logger.debug(this, "\ttrainingFileRead=" + trainingFileRead);
			return trainingFile && trainingFileRead;
		} catch (DataBeanAccessException e) {
			logger.error(this, e.toString(), e);
			throw new StepExecutionException(e);
		}
	}

	@Override
	public DataBean run(DataBean data, StepProcessObserver listener)
			throws StepExecutionException {
		try {
			if (trainingFile.exists()){
				takeShortcut(data);
			}
			else {
				assignState();
				final ArrayList<? extends FASTASequence> fastas = data
						.getVerifiedGenesFasta();
				final ArrayList<? extends GTFElement> elements = data
						.getVerifiedGenesGtf();
				final boolean b = state.run(fastas, elements);
				if (b){
					logger.info(this, "training sucessfull");
					data.setConradTrainingFile(state.getResult());
					setSuccess(true);
				}
				else
					handleFailure(data);
			}

		} catch (DataBeanAccessException e) {
			logger.error(this, e.toString(), e);
			throw new StepExecutionException(e);
		}
		return data;
	}

	private void takeShortcut(DataBean data) throws DataBeanAccessException {
		logger.info(this, "training file already exists, taking short cut ("
				+ trainingFile + ")");
		logger.info(this, "training sucessfull");
		data.setConradTrainingFile(trainingFile);
		setSuccess(true);
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

	private void handleFailure(DataBean data) {
		logger.warn(this, "training not sucessfull, will not update data");
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
