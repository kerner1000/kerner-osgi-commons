package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep.State;

/**
 * @Threadsave
 *
 */
public class ServerMonitorImpl implements ServerMonitor {

	private final Map<AbstractStep, AbstractStep.State> stepStates = new ConcurrentHashMap<AbstractStep, AbstractStep.State>();
//	private final Map<AbstractStep, Boolean> stepSuccess = new ConcurrentHashMap<AbstractStep, Boolean>();

	public ServerMonitorImpl() {

	}
	
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	private void printStepStates(AbstractStep step) {
		System.out.println(this + ": current step states:");
		for (AbstractStep s : stepStates.keySet()) {
			System.out.print("\t" + s + "=" + stepStates.get(s) + "\t" + s.getSuccess());
			if (step.equals(s)) {
				System.out.print("\t(changed)");
			}
			System.out.println();
		}
	}
	
	private void checkConsistity(AbstractStep step, AbstractStep.State expectedCurrentState, AbstractStep.State newState){
		AbstractStep.State state = stepStates.get(step);
		if(state == null){
			state = AbstractStep.State.LOOSE;
			System.out.println(this + ": step " + step + " new, assuming state " + state);
		}
		if (!state.equals(expectedCurrentState)) {
			System.out.println(this
					+ ": warning, inconsistent step state mapping for step " + step + "\n\t step state changed from " + stepStates.get(step) + " to " + newState);
		}
	}
	
	private void changeStepState(AbstractStep step, AbstractStep.State newState){
		stepStates.put(step, newState);
		printStepStates(step);
	}

	public void stepFinished(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.DONE;
		final AbstractStep.State expectedCurrentState = State.RUNNING;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public void stepRegistered(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.REGISTERED;
		final AbstractStep.State expectedCurrentState = AbstractStep.State.LOOSE;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public void stepStarted(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.RUNNING;
		final AbstractStep.State expectedCurrentState = AbstractStep.State.WAIT_FOR_REQ;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public void stepChecksNeedToRun(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.CHECK_NEED_TO_RUN;
		final AbstractStep.State expectedCurrentState = AbstractStep.State.REGISTERED;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
		
	}

	public void stepWaitForReq(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.WAIT_FOR_REQ;
		final AbstractStep.State expectedCurrentState = AbstractStep.State.CHECK_NEED_TO_RUN;
		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}
	
	
}
