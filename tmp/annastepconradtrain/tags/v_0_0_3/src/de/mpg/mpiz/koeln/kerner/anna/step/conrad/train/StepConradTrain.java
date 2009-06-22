package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

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
	private final static String RUN_KEY = PROPERTIES_KEY_PREFIX + "run";
	private final static String RUN_VALUE_LOCAL = "local";
	private final static String RUN_VALUE_LSF = "lsf";
	private final static String DEFAULT_RUN_VALUE = RUN_VALUE_LOCAL;

	// TODO make final again
	private AbstractRunStateTraining state;
	private File conradWorkingDir, stepWorkingDir;
	private String trainingFileName, runEnv;

	public StepConradTrain() {
		// TODO remove try catch
		try {
			assignProperties();
			validateProperties();
			printProperties();
			assignState();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void assignProperties() {
		conradWorkingDir = new File(super.getStepProperties().getProperty(
				ConradConstants.CONRAD_DIR_KEY));
		stepWorkingDir = new File(super.getStepProperties().getProperty(
				WORKING_DIR_KEY));
		trainingFileName = super.getStepProperties().getProperty(
				TRAINING_FILE_NAME_KEY);
		runEnv = super.getStepProperties().getProperty(RUN_KEY,
				DEFAULT_RUN_VALUE);
	}

	private void validateProperties() throws StepExecutionException {
		if (!conradWorkingDir.exists() || !conradWorkingDir.canRead())
			throw new StepExecutionException("cannot access conrad working dir");
		if (!checkWorkingDir(stepWorkingDir))
			throw new StepExecutionException("cannot access step working dir");
	}

	private void printProperties() {
		System.out.println(this + ": created. Properties:");
		System.out.println(this + ":\tstepWorkingDir=" + stepWorkingDir);
		System.out.println(this + ":\tconradWorkingDir=" + conradWorkingDir);
		System.out.println(this + ":\ttraiingFileName=" + trainingFileName);
		System.out.println(this + ":\trunEnv=" + runEnv);
	}

	private void assignState() {
		if (runEnv.equalsIgnoreCase(RUN_VALUE_LSF)) {
			state = new RunStateLSF(stepWorkingDir, conradWorkingDir,
					trainingFileName);
			System.out.println(this + ": going to run on LSF");
		} else if (runEnv.equalsIgnoreCase(RUN_VALUE_LOCAL)) {
			state = new RunStateLocal(stepWorkingDir, conradWorkingDir,
					trainingFileName);
			System.out.println(this + ": going to run locally");
		} else {
			state = new RunStateLocal(stepWorkingDir, conradWorkingDir,
					trainingFileName);
			System.out.println(this + ": unrecognized running env \""
					+ "\", going to run locally");
		}
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
		// TODO try catch
		try {
			final ArrayList<? extends FASTASequence> fastas = data
					.getVerifiedGenesFasta();
			final ArrayList<? extends GTFElement> elements = data
					.getVerifiedGenesGtf();
//			System.out.println("++++++++++++++++++++++");
//			System.out.println(this + ":checkRequirements: fastas=" + fastas);
//			System.out.println(this + ":checkRequirements: elements=" + elements);
//			System.out.println("++++++++++++++++++++++");
			return (fastas != null && fastas.size() != 0 && elements != null && elements
					.size() != 0);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new StepExecutionException(t);
		}
	}

	@Override
	public DataBean run(DataBean data, StepProcessObserver listener)
			throws StepExecutionException {
		try {
			final ArrayList<? extends FASTASequence> fastas = data
					.getVerifiedGenesFasta();
			final ArrayList<? extends GTFElement> elements = data
					.getVerifiedGenesGtf();
			final boolean b = state.run(fastas, elements);
			if (b == false) {
				System.out.println(this
						+ " training not sucessfull, cannot update data");
			} else {
				final File trainingFile = state.getResult();
				System.out.println(this
						+ " training sucessfull, created training file "
						+ trainingFile);
				data.setConradTrainingFile(trainingFile);
				setSuccess(true);
			}
		} catch (Exception e) {
			throw new StepExecutionException(e);
		}
		return data;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean needToRun(DataBean data) throws StepExecutionException {
		// /**

		try {
			return (data.getConradTrainingFile() == null);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}

		// */

		// return true;
	}

}
