package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;

class LocalSepExecutor extends AbstractStepExecutor {

	LocalSepExecutor(AbstractStep step, Server server) {
		super(step, server);
	}

	public Boolean call() throws Exception {
		final boolean b = checkCanBeSkipped();
		boolean success = true;
		if (b) {
			
			System.out.println(this + ": step " + step
					+ " does not need to run, skipping");
			step.setSkipped(true);
			
		} else {
			System.out.println(this + ": step " + step + " needs to run");
			waitForReq();
			success = run();
		}
		System.out.println(this + ": step " + step + " done");
		server.getStepStateObserver().stepFinished(step, success);
		return success;
	}

	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ step.getClass().getSimpleName();
	}
}
