package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;

public class ConradTrainActivator extends AbstractStep {

	private final static String PROPERTIES_KEY_PREFIX = "anna.step.conrad.train.";
	private final static String CONRAD_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "conradWorkingDir";
	private final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "workingDir";
	private final static String TRAINING_FILE_NAME_KEY = PROPERTIES_KEY_PREFIX
			+ "trainingFileName";
	private final static String RUN_KEY = PROPERTIES_KEY_PREFIX + "run";
	private final static String RUN_VALUE_LOCAL = "local";
	private final static String RUN_VALUE_LSF = "lsf";
	private final static String DEFAULT_RUN_VALUE = RUN_VALUE_LOCAL;
	
	// TODO make final again
	private AbstractRunState state;

	public ConradTrainActivator() {
		// TODO remove try catch
		try {
			final String conradWorkingDir = super.getStepProperties()
					.getProperty(CONRAD_DIR_KEY);
			final File conradWorkingDirFile = new File(conradWorkingDir);
			System.out.println(this + ": got conrad working dir: "
					+ conradWorkingDirFile.getAbsolutePath());
			if (!conradWorkingDirFile.exists()
					|| !conradWorkingDirFile.canRead()) {
				System.out.println(this + ": cannot access conrad working dir");
				throw new RuntimeException("cannot access conrad working dir");
			}

			final String workingDirString = super.getStepProperties()
			.getProperty(WORKING_DIR_KEY);
			System.out.println(this + ": got working dir: " + workingDirString);
			final File workingDir = new File(workingDirString);
			if (!checkWorkingDir(workingDir)) {
				System.out.println(this + ": cannot access step working dir");
				throw new RuntimeException("cannot access step working dir");
			}

			final String trainingFileName = super.getStepProperties()
					.getProperty(TRAINING_FILE_NAME_KEY);
			System.out.println(this + ": got training file name: "
					+ trainingFileName);

			final String runValue = super.getStepProperties().getProperty(
					RUN_KEY, DEFAULT_RUN_VALUE);
			System.out.println(this + ": got running env: " + runValue);
			if (runValue.equalsIgnoreCase(RUN_VALUE_LSF)) {
				state = new RunStateLSF(workingDir, conradWorkingDirFile,
						trainingFileName);
				System.out.println(this + ": going to run on LSF");
			} else if (runValue.equalsIgnoreCase(RUN_VALUE_LOCAL)) {
				state = new RunStateLocal(workingDir, conradWorkingDirFile,
						trainingFileName);
				System.out.println(this + ": going to run locally");
			} else {
				state = new RunStateLocal(workingDir, conradWorkingDirFile,
						trainingFileName);
				System.out.println(this + ": unrecognized running env \""
						+ "\", going to run locally");
			}

		} catch (Throwable t) {
			t.printStackTrace();

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
	public boolean checkRequirements(DataBean data) {
		try {
			return (data.getVerifiedGenesFasta() != null
					&& data.getVerifiedGenesFasta().size() != 0
					&& data.getVerifiedGenesGtf() != null && data
					.getVerifiedGenesGtf().size() != 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(this + " could not access data ");
			return false;
		}
	}

	@Override
	public DataBean run(DataBean data) throws Exception {
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
		}
		return data;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
