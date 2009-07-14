package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import de.mpg.mpiz.koeln.kerner.anna.abstractstep.AbstractStep;

public interface StepStateObserver {
	void stepRegistered(AbstractStep step);

	void stepStarted(AbstractStep step);

	void stepFinished(AbstractStep step, boolean success);

	void stepChecksNeedToRun(AbstractStep step);

	void stepWaitForReq(AbstractStep step);
}
