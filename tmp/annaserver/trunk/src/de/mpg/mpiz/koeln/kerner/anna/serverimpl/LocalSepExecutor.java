package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;

class LocalSepExecutor extends AbstractStepExecutor {

	LocalSepExecutor(AbstractStep step, Server server) {
		super(step, server);
	}

	public Boolean call() throws Exception {
		boolean success = true;
		server.getStepStateObserver().stepChecksNeedToRun(step);
		final boolean b = step.canBeSkipped(server.getDataProxyProvider()
				.getService());
		if (b) {
			logger.info(this, "step " + step
					+ " does not need to run, skipping");
			step.setSkipped(true);
			server.getStepStateObserver().stepFinished(step, success);
			return success;
		}
		logger.debug(this, "step " + step + " needs to run");
		server.getStepStateObserver().stepWaitForReq(step);
		synchronized (server) {
			try {
				while (!step.requirementsSatisfied(server
						.getDataProxyProvider().getService())) {
					logger.debug(this, "requirements for step " + step
							+ " not satisfied, putting it to sleep");
					server.wait();
				}
				logger.debug(this, "requirements for step " + step
						+ " satisfied");
			} catch (InterruptedException e) {
				logger.error(this, e, e);
				logger.debug(this, "notifying others");
				server.notifyAll();
				throw new StepExecutionException(e);
			}
			logger.debug(this, "notifying others");
			server.notifyAll();
		}
		success = runStep();
		stepFinished(success);
		return success;
	}
	
	private boolean runStep() throws StepExecutionException {
		final StepProcessObserver listener = new StepProgressObserverImpl();
		logger.debug(this, "step " + step + "running");
		server.getStepStateObserver().stepStarted(step);
		return step
				.run(server.getDataProxyProvider().getService(), listener);
	}

	private void stepFinished(boolean success){
		logger.debug(this, "step " + step + "done running");
		server.getStepStateObserver().stepFinished(step, success);
		synchronized (server) {
			logger.debug(this, "notifying others");
			server.notifyAll();
		}
	}

	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ step.getClass().getSimpleName();
	}
}
