package de.mpg.mpiz.koeln.kerner.anna.step.conrad.predict;

import java.io.File;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessListener;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;
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

	// TODO make final again
	private File conradWorkingDir, stepWorkingDir;
	private String runEnv;
	private AbstractRunStatePredicting state;

	public StepConradPredict() {
		// TODO remove try catch
		try {
			assignProperties();
			validateProperties();
			printProperties();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void printProperties() {
		System.out.println(this + ": created. Properties:");
		System.out.println(this + ":\tstepWorkingDir=" + stepWorkingDir);
		System.out.println(this + ":\tconradWorkingDir=" + conradWorkingDir);
		System.out.println(this + ":\trunEnv=" + runEnv);
	}

	private void validateProperties() throws StepExecutionException {
		if (!conradWorkingDir.exists() || !conradWorkingDir.canRead())
			throw new StepExecutionException("cannot access conrad working dir");
		if (!checkWorkingDir(stepWorkingDir))
			throw new StepExecutionException("cannot access step working dir");

	}

	private void assignProperties() {
		conradWorkingDir = new File(super.getStepProperties().getProperty(
				ConradConstants.CONRAD_DIR_KEY));
		stepWorkingDir = new File(super.getStepProperties().getProperty(
				WORKING_DIR_KEY));
		runEnv = super.getStepProperties().getProperty(RUN_KEY,
				DEFAULT_RUN_VALUE);
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
	public boolean checkRequirements(DataBean data)
			throws StepExecutionException {
		try {
			return (data.getConradTrainingFile() != null);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean needToRun(DataBean data) throws StepExecutionException {
		// TODO size may be zero, if nothing was found
		try {
			return (data.getPredictedGenesGtf() == null || data
					.getPredictedGenesGtf().size() == 0);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

	@Override
	public DataBean run(DataBean data, StepProcessListener listener) throws StepExecutionException {
		try {
			final File trainingFile = data.getConradTrainingFile();
			if (runEnv.equalsIgnoreCase(RUN_VALUE_LSF)) {
				state = new RunStateLSF(stepWorkingDir, conradWorkingDir,
						trainingFile);
				System.out.println(this + ": going to run on LSF");
			} else if (runEnv.equalsIgnoreCase(RUN_VALUE_LOCAL)) {
				state = new RunStateLocal(stepWorkingDir, conradWorkingDir,
						trainingFile);
				System.out.println(this + ": going to run locally");
			} else {
				state = new RunStateLocal(stepWorkingDir, conradWorkingDir,
						trainingFile);
				System.out.println(this + ": unrecognized running env \""
						+ "\", going to run locally");
			}
			final boolean b = state.run(trainingFile);
			if(b){
				System.out.println(this
						+ " predicting sucessfull, created predicting file "
						+ trainingFile);
				data.setPredictedGenesGtf(state.getResult());
				setSuccess(true);
			} else {
				System.out.println(this
						+ " predicting not sucessfull, cannot update data");
			}
			return data;
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
