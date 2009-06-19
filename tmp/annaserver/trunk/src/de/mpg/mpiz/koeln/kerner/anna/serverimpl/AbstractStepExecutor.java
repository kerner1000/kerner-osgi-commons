package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

abstract class AbstractStepExecutor implements Callable<Boolean> {

	protected final AbstractStep step;
	protected final Server server;

	AbstractStepExecutor(AbstractStep step, Server server) {
		this.step = step;
		this.server = server;
	}
	
	protected boolean checkNeedToRun() throws StepExecutionException{
			try {
				server.getServermonitor().stepChecksNeedToRun(step);
				return step.needToRun(server.getDataProxyProvider()
						.getDataProxy().getDataBean());
			} catch (StepExecutionException e) {
				throw new StepExecutionException(e);
			} catch (DataBeanAccessException e) {
				throw new StepExecutionException(e);
			}
	}

	protected void waitForReq() throws StepExecutionException {
		synchronized (server) {
			try {
				server.getServermonitor().stepWaitForReq(step);
				while (!step.checkRequirements(server.getDataProxyProvider()
						.getDataProxy().getDataBean())) {
					System.out.println(this + ": requirements for step " + step
							+ " not satisfied, putting it to sleep");
					server.wait();
				}
				System.out.println(this + ": requirements for step " + step
						+ " satisfied");
			} catch (DataBeanAccessException e) {
				throw new StepExecutionException(e);
			} catch (InterruptedException e) {
				throw new StepExecutionException(e);
			}
		}
		synchronized (server) {
		server.notifyAll();
		}
	}
	
	protected void run() throws StepExecutionException {
		// TODO remove try catch
		try{
			server.getServermonitor().stepStarted(step);
		System.out.println(this + ": running step " + step);
		final DataBean data = step.run(server.getDataProxyProvider()
				.getDataProxy().getDataBean());
		System.out.println(this + ": step " + step
				+ " finished, updateing data");
		server.getDataProxyProvider().getDataProxy().updateDataBean(data);
		server.getServermonitor().stepFinished(step);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
}
