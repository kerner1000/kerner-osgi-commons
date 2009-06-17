package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;

public class ConradTrainActivator extends AbstractStep {

	private final static String RUN_KEY = "run";
	private final static String RUN_VALUE_LOCAL = "local";
	private final static String RUN_VALUE_LSF = "lsf";
	private final static String DEFAULT_RUN_VALUE = RUN_VALUE_LOCAL;
	private final AbstractRunState state;

	public ConradTrainActivator() {
		final String runValue = super.getStepProperties().getProperty(RUN_KEY,
				DEFAULT_RUN_VALUE);
		System.out.println(this + ": got running env: " + runValue);
		if (runValue.equalsIgnoreCase(RUN_VALUE_LSF)) {
			state = new RunStateLSF();
			System.out.println(this + ": going to run on LSF");
		} else if (runValue.equalsIgnoreCase(RUN_VALUE_LOCAL)) {
			state = new RunStateLocal();
			System.out.println(this + ": going to run locally");
		} else {
			state = new RunStateLocal();
			System.out.println(this + ": unrecognized running env \""
					+ "\", going to run locally");
		}
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
