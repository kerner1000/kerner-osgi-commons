package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

abstract class AbstractStepExecutor implements Callable<Boolean> {

	protected final AbstractStep step;
	protected final Server server;
//	private final ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();

	AbstractStepExecutor(AbstractStep step, Server server) {
		this.step = step;
		this.server = server;
	}
	
	protected boolean checkNeedToRun() throws StepExecutionException{
			try {
				server.getStepStatemonitor().stepChecksNeedToRun(step);
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
				server.getStepStatemonitor().stepWaitForReq(step);
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
			server.getStepStatemonitor().stepStarted(step);
		System.out.println(this + ": running step " + step);
		final StepProcessObserver listener = new StepProgressObserverImpl();
		final DataBean data = step.run(server.getDataProxyProvider()
				.getDataProxy().getDataBean(), listener);
		System.out.println(this + ": step " + step
				+ " finished, updateing data");
		server.getDataProxyProvider().getDataProxy().updateDataBean(data);
		server.getStepStatemonitor().stepFinished(step);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
}
