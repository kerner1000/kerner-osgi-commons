package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

abstract class AbstractStepExecutor implements Callable<Boolean> {
	
	protected final AbstractStep step;
	protected final Server server;
	
	AbstractStepExecutor(AbstractStep step, Server server){
		this.step = step;
		this.server = server;
	}
	
	protected void waitForReq() throws InterruptedException, DataBeanAccessException {
		synchronized (server) {
			while (!step.checkRequirements(server.getDataProxyProvider().getDataProxy().getDataBean())) {
				System.out.println(this + ": requirements for step "
						+ step + " not satisfied, putting it to sleep");
				server.wait();
			}
			System.out.println(this + ": requirements for step " + step +" satisfied");
		}
	}
}
