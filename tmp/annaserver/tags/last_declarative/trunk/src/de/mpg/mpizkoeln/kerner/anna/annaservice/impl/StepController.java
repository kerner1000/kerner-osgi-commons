package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.io.IOException;
import java.util.concurrent.Callable;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBeanAccessException;
import de.mpg.mpizkoeln.kerner.anna.core.DataBeanProxy;

class StepController implements Callable<Void> {

	private final AbstractStep step;

	StepController(AbstractStep step) {
		this.step = step;
	}

	private void waitForReqs() throws InterruptedException {
		synchronized (step) {
			while (!step.checkRequirements()) {
				AnnaServiceImpl.LOGGER.debug(this, "Requirements for step " + step
						+ " not satisfied. Putting it to sleep.");
				this.wait();
			}
		}
	}

	private void activate() throws Exception {
		AnnaServiceImpl.LOGGER.debug(this, "Activating step " + step);
		if (step.getEnvironment().equals(AbstractStep.Environment.LOCAL)) {
			// call "call()" directly, so that this method will run in
			// current thread.
			new LocalStepExecutor(step).call();
		} else if (step.getEnvironment().equals(AbstractStep.Environment.LSF)) {
			// call "call()" directly, so that this method will run in
			// current thread.
			new RemoteStepExecutor(step).call();
		} else
			throw new RuntimeException("Cannot be");
	}

	public Void call() throws Exception {
		try {
			waitForReqs();
			activate();
			AnnaServiceImpl.LOGGER.debug(this, "Step " + step + " finished");
		} catch (InterruptedException e) {
			AnnaServiceImpl.LOGGER.debug(this, "Step " + step
					+ " has been interrupted");
		} finally {
			AnnaServiceImpl.LOGGER.debug(this, "Step " + step
					+ " is done, notifying others");
			this.notifyAll();
		}
		return null;
	}
}
