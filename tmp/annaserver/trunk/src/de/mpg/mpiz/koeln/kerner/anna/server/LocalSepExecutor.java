package de.mpg.mpiz.koeln.kerner.anna.server;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;

class LocalSepExecutor extends AbstractStepExecutor {

	LocalSepExecutor(AbstractStep step) {
		super(step);
	}

	public Boolean call() throws Exception {
		waitForReq();
		run();
		ServerActivator.LOGGER.debug(this, "step " + step + " done");
		notifyAll();
		return true;
	}
	
	private void run() throws Exception {
		synchronized (step) {
			ServerActivator.LOGGER.debug(this, "running step " + step);
			step.run();
		}
	}
}
