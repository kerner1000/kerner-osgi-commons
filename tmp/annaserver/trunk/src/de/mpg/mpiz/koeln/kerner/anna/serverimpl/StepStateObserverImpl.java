package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.HashMap;
import java.util.Map;

import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep.State;

/**
 * @Threadsave
 * 
 */
public class StepStateObserverImpl implements StepStateObserver {

	private final Map<AbstractStep, AbstractStep.State> stepStates = new HashMap<AbstractStep, AbstractStep.State>();
	private final Map<AbstractStep, Boolean> stepSuccesses = new HashMap<AbstractStep, Boolean>();

	public StepStateObserverImpl() {

	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	private synchronized void printStepStates(AbstractStep lastChangedStep) {
		System.out.println("++++++++++++++++++++++++");
		System.out.println(this + ": current step states:");
		for (AbstractStep s : stepStates.keySet()) {
			final String s1 = s.toString();
			final String s2 = "state=" + stepStates.get(s);
			final String s3 = "skipped=" + s.wasSkipped();
			String s4 = "success=" + stepSuccesses.get(s);
			System.out.printf("\t%-30s\t%-22s\t%-10s\t%-10s", s1, s2, s3 ,s4);
//			System.out.print("\t" + s + "\t:state=" + stepStates.get(s)
//					+ "\tskipped=" + s.wasSkipped() + "\tsuccess="
//					+ s.getSuccess());
			if (lastChangedStep.equals(s)) {
				System.out.print("\t(changed)");
			}
			System.out.println();
		}
		System.out.println("++++++++++++++++++++++++");
	}

	private synchronized void checkConsistity(AbstractStep step,
			AbstractStep.State expectedCurrentState, AbstractStep.State newState) {
		AbstractStep.State state = stepStates.get(step);
		if (state == null) {
			state = AbstractStep.State.LOOSE;
			System.out.println(this + ": step " + step
					+ " new, assuming state " + state);
		}
		if (!state.equals(expectedCurrentState)) {
			// ignore inconsistency due to skipping of step
			
//			System.out.println(this
//					+ ": warning, inconsistent step state mapping for step "
//					+ step + "\n\t step state changed from "
//					+ stepStates.get(step) + " to " + newState);
		}
	}

	private synchronized void changeStepState(AbstractStep step, AbstractStep.State newState) {
		stepStates.put(step, newState);
		printStepStates(step);
	}

	public synchronized void stepFinished(AbstractStep step, boolean success) {
		final AbstractStep.State newState = AbstractStep.State.DONE;
		final AbstractStep.State expectedCurrentState = State.RUNNING;
		stepSuccesses.put(step, success);
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public synchronized void stepRegistered(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.REGISTERED;
		final AbstractStep.State expectedCurrentState = AbstractStep.State.LOOSE;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public synchronized void stepStarted(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.RUNNING;
		final AbstractStep.State expectedCurrentState = AbstractStep.State.WAIT_FOR_REQ;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public synchronized void stepChecksNeedToRun(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.CHECK_NEED_TO_RUN;
		final AbstractStep.State expectedCurrentState = AbstractStep.State.REGISTERED;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public synchronized void stepWaitForReq(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.WAIT_FOR_REQ;
		final AbstractStep.State expectedCurrentState = AbstractStep.State.CHECK_NEED_TO_RUN;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

}
