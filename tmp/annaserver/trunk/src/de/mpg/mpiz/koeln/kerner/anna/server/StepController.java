package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBean;

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

	private void run() throws InterruptedException {
		synchronized (step) {
			ServerActivator.LOGGER.debug(this, "running step " + step);
			step.run(getDataBean());
		}
	}

	private void waitForReq() throws InterruptedException {
		synchronized (step) {
			while (!step.checkRequirements(getDataBean())) {
				ServerActivator.LOGGER.debug(this, "requirements for step "
						+ step + " not satisfied, putting it to sleep");
				this.wait();
			}
		}
	}

	private DataBean getDataBean() throws InterruptedException {
		synchronized (step) {
			return step.getDataBean();
		}
	}
}
