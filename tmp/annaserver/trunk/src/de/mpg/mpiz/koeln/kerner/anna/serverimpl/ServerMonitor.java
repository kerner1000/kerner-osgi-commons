package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;

public interface ServerMonitor {
	void stepRegistered(AbstractStep step);
	void stepStarted(AbstractStep step);
	void stepFinished(AbstractStep step);
	void stepChecksNeedToRun(AbstractStep step);
	void stepWaitForReq(AbstractStep step);
}
