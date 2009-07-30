package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.abstractstep.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.abstractstep.AbstractStep.State;

/**
 * @ThreadSave
 * @cleaned 2009-07.29
 * @author Alexander Kerner
 *
 */
public class StepStateObserverImpl implements StepStateObserver {

	private final Map<AbstractStep, AbstractStep.State> stepStates = new HashMap<AbstractStep, AbstractStep.State>();
	private final Map<AbstractStep, Boolean> stepSuccesses = new HashMap<AbstractStep, Boolean>();
	private final static String PRE_LINE =  "++++++ current states ++++++++";
	private final static String POST_LINE = "++++++++++++++++++++++++++++++";
	private final LogDispatcher logger;
	
	public StepStateObserverImpl(LogDispatcher logger) {
		this.logger = logger;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	private synchronized void printStepStates(AbstractStep lastChangedStep) {
		logger.info(this, FileUtils.NEW_LINE);
		logger.info(this, PRE_LINE);
		
		for (AbstractStep s : stepStates.keySet()) {
			final String s1 = s.toString();
			final String s2 = "state=" + stepStates.get(s);
			final String s3 = "skipped=" + s.wasSkipped();
			String s4 = "success=" + stepSuccesses.get(s);
			
			final StringBuilder sb = new StringBuilder();
			// TODO better: String.format();
			final Formatter f = new Formatter();
			sb.append(f.format("\t%-28s\t%-22s\t%-10s\t%-10s", s1, s2, s3, s4).toString());
			
			if (lastChangedStep.equals(s)) {
				sb.append("\t(changed)");
			}
//			sb.append(FileUtils.NEW_LINE);
			logger.info(this, sb.toString());
		}
		logger.info(this, POST_LINE);
		logger.info(this, FileUtils.NEW_LINE);
	}

	private void changeStepState(AbstractStep step,
			AbstractStep.State newState) {
		if(step.getState().equals(AbstractStep.State.ERROR))
			newState = AbstractStep.State.ERROR;
		stepStates.put(step, newState);
		printStepStates(step);
	}

	public synchronized void stepFinished(AbstractStep step, boolean success) {
		final AbstractStep.State newState = AbstractStep.State.DONE;
//		final AbstractStep.State expectedCurrentState = State.RUNNING;
		stepSuccesses.put(step, success);
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public synchronized void stepRegistered(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.REGISTERED;
//		final AbstractStep.State expectedCurrentState = AbstractStep.State.LOOSE;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public synchronized void stepStarted(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.RUNNING;
//		final AbstractStep.State expectedCurrentState = AbstractStep.State.WAIT_FOR_REQ;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public synchronized void stepChecksNeedToRun(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.CHECK_NEED_TO_RUN;
//		final AbstractStep.State expectedCurrentState = AbstractStep.State.REGISTERED;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public synchronized void stepWaitForReq(AbstractStep step) {
		final AbstractStep.State newState = AbstractStep.State.WAIT_FOR_REQ;
//		final AbstractStep.State expectedCurrentState = AbstractStep.State.CHECK_NEED_TO_RUN;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

}
