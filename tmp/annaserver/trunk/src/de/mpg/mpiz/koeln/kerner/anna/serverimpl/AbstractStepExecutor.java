package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.core.DataBeanProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

abstract class AbstractStepExecutor implements Callable<Boolean> {
	
	protected final AbstractStep step;
	protected final DataBeanProvider provider;
	
	AbstractStepExecutor(AbstractStep step, DataBeanProvider provider){
		this.step = step;
		this.provider = provider;
	}
	
	protected void waitForReq() throws InterruptedException, DataBeanAccessException {
		synchronized (provider) {
			while (!step.checkRequirements(provider.getDataProxy().getDataBean())) {
				System.out.println(this + ": requirements for step "
						+ step + " not satisfied, putting it to sleep");
				provider.wait();
			}
			System.out.println(this + ": requirements for step " + step +" satisfied");
		}
	}
}
