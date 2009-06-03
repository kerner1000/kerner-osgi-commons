package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataProxy;

class StepController implements Callable<Boolean> {

	private final AbstractStep step;

	StepController(AbstractStep step) {
		this.step = step;
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
			step.run(getDataProxy());
		}
	}

	private void waitForReq() throws InterruptedException, DataBeanAccessException {
		synchronized (step) {
			while (!step.checkRequirements(getDataProxy())) {
				ServerActivator.LOGGER.debug(this, "requirements for step "
						+ step + " not satisfied, putting it to sleep");
				this.wait();
			}
		}
	}

	private DataProxy getDataProxy() throws InterruptedException, DataBeanAccessException {
		synchronized (step) {
			return step.getDataProxy();
		}
	}
}
