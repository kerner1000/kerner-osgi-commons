package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBeanAccessException;

abstract class AbstractStepExecutor implements Callable<Boolean> {
	
	protected final AbstractStep step;
	protected final ServerActivator serverActivator;
	
	AbstractStepExecutor(AbstractStep step, ServerActivator serverActivator){
		this.step = step;
		this.serverActivator = serverActivator;
	}
	
	protected void waitForReq() throws InterruptedException, DataBeanAccessException {
		synchronized (step) {
			while (!step.checkRequirements(serverActivator.getDataProxy().getDataBean())) {
				ServerActivator.LOGGER.debug(this, "requirements for step "
						+ step + " not satisfied, putting it to sleep");
				Thread.currentThread().wait();
			}
			ServerActivator.LOGGER.debug(this, "requirements satisfied");
		}
	}
}
