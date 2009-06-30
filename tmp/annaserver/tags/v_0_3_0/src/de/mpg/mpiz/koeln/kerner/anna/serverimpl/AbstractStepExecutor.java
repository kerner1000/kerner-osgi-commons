package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;

abstract class AbstractStepExecutor implements Callable<Boolean> {

	protected final AbstractStep step;
	protected final Server server;

	AbstractStepExecutor(AbstractStep step, Server server) {
		this.step = step;
		this.server = server;
	}

	protected boolean checkCanBeSkipped() throws StepExecutionException {
		try {
			server.getStepStateObserver().stepChecksNeedToRun(step);
			return step.canBeSkipped(server.getDataProxyProvider().getService());
		} catch (StepExecutionException e) {
			throw new StepExecutionException(e);
		}
	}

	protected void waitForReq() throws StepExecutionException {
		synchronized (server) {
			try {
				server.getStepStateObserver().stepWaitForReq(step);
				while (!step.requirementsSatisfied(server
						.getDataProxyProvider().getService())) {
					System.out.println(this + ": requirements for step " + step
							+ " not satisfied, putting it to sleep");
					server.wait();
				}
				System.out.println(this + ": requirements for step " + step
						+ " satisfied");
			} catch (InterruptedException e) {
				throw new StepExecutionException(e);
			} finally {
				synchronized (server) {
					server.notifyAll();
				}
			}
		}
	}

	protected boolean run() throws StepExecutionException {
		// TODO remove try catch
		try {
			server.getStepStateObserver().stepStarted(step);
			System.out.println(this + ": running step " + step);
			final StepProcessObserver listener = new StepProgressObserverImpl();
			final boolean success = step.run(server.getDataProxyProvider().getService(),
					listener);
			System.out.println(this + ": step " + step + " finished");
			return success;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		} finally {
			synchronized (server) {
				server.notifyAll();
			}
		}
	}
}
