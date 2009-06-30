package de.mpg.mpiz.koeln.kerner.anna.step.conrad.predict;

import java.io.File;
import java.io.IOException;

import org.bioutils.fasta.FASTAFile;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

public class StepConradPredict extends AbstractStep {

	private final static String PROPERTIES_KEY_PREFIX = ConradConstants.PROPERTIES_KEY_PREFIX
			+ "predict.";
	private final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "workingDir";
	private final static String RUN_KEY = PROPERTIES_KEY_PREFIX + "run";
	private final static String RUN_VALUE_LOCAL = "local";
	private final static String RUN_VALUE_LSF = "lsf";
	private final static String DEFAULT_RUN_VALUE = RUN_VALUE_LOCAL;

	private File conradWorkingDir, stepWorkingDir;
	private String runEnv;
	private AbstractRunStatePredicting state;
	private LogDispatcher logger;

	public StepConradPredict() {
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
		runEnv = super.getStepProperties().getProperty(RUN_KEY,
				DEFAULT_RUN_VALUE);
	}

	private void validateProperties() throws StepExecutionException {
		if (!conradWorkingDir.exists() || !conradWorkingDir.canRead())
			throw new StepExecutionException("cannot access conrad working dir");
		if (!checkDir(stepWorkingDir))
			throw new StepExecutionException("cannot access step working dir");

	}

	private boolean checkDir(final File workingDir) {
		if (!workingDir.exists()) {
			logger.info(this, workingDir + " does not exist, creating");
			final boolean b = workingDir.mkdirs();
			return b;
		}
		return workingDir.canWrite();
	}

	private void printProperties() {
		logger.debug(this, "created, properties:");
		logger.debug(this, "\tstepWorkingDir=" + stepWorkingDir);
		logger.debug(this, "\tconradWorkingDir=" + conradWorkingDir);
		logger.debug(this, "\trunEnv=" + runEnv);
	}

	@Override
	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		// TODO try catch
		try {
			final boolean trainingFile = (data
					.getConradTrainingFile() != null && data					.getConradTrainingFile().exists());
			final boolean trainingFileRead = (data
					.getConradTrainingFile() != null && data
					.getConradTrainingFile().canRead());
			final boolean inputSequences = (data
					.getInputSequences() != null);
			final boolean inputSequencesSize = (data
					.getInputSequences().size() != 0);
			logger.debug(this, "requirements:");
			logger.debug(this, "\ttrainingFile=" + trainingFile);
			logger.debug(this, "\ttrainingFileRead=" + trainingFileRead);
			logger.debug(this, "\tinputSequences=" + inputSequences);
			logger.debug(this, "\tinputSequencesSize=" + inputSequencesSize);
			return (trainingFile && trainingFileRead && inputSequences && inputSequencesSize);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean canBeSkipped(DataProxy data)
			throws StepExecutionException {

		// TODO size may be zero, if nothing was found
		try {
			final boolean predictedGtf = (data
					.getPredictedGenesGtf() != null);
			final boolean predictedGtfSize = (data
					.getPredictedGenesGtf().size() != 0);
			logger.debug(this, "need to run:");
			logger.debug(this, "\tpredictedGtf=" + predictedGtf);
			logger.debug(this, "\tpredictedGtfSize=" + predictedGtfSize);
			return (predictedGtf && predictedGtfSize);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		boolean success = false;
		try {
			writeInputSequencesToFile(data);
			final File trainingFile = data
					.getConradTrainingFile();
			state = createState(trainingFile);
			success = state.run(trainingFile);
			if (success)
				data.setPredictedGenesGtf(state.getResult());
			return success;
		} catch (DataBeanAccessException e) {
			logger.error(this, e.toString(), e);
			return success;
		} catch (IOException e) {
			logger.error(this, e.toString(), e);
			return success;
		}
	}

	private AbstractRunStatePredicting createState(final File trainingFile)
			throws DataBeanAccessException {
		AbstractRunStatePredicting state;
		if (runEnv.equalsIgnoreCase(RUN_VALUE_LSF)) {
			state = runOnLSF(trainingFile);
		} else if (runEnv.equalsIgnoreCase(RUN_VALUE_LOCAL)) {
			state = runLocally(trainingFile);
		} else {
			logger.warn(this, "unrecognized running env \"" + runEnv
					+ "\", going to run locally");
			state = runLocally(trainingFile);
		}
		return state;
	}

	private AbstractRunStatePredicting runLocally(File trainingFile) {
		logger.debug(this, "going to run locally");
		return new RunStateLocal(stepWorkingDir, conradWorkingDir, trainingFile);
	}

	private AbstractRunStatePredicting runOnLSF(File trainingFile) {
		logger.debug(this, "going to run on LSF");
		return new RunStateLSF(stepWorkingDir, conradWorkingDir, trainingFile);
	}

	private void writeInputSequencesToFile(DataProxy data)
			throws IOException, DataBeanAccessException {
		final File refFile = new File(stepWorkingDir, "ref.fasta");
		refFile.deleteOnExit();
		new FASTAFile(data.getInputSequences())
				.writeToFile(refFile);
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
