package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;

abstract class AbstractStepExecutor implements Callable<Boolean> {

	protected final AbstractStep step;
	protected final Server server;
	protected final LogDispatcher logger;

	AbstractStepExecutor(AbstractStep step, Server server) {
		this.step = step;
		this.server = server;
		this.logger = new ConsoleLogger();
	}

	AbstractStepExecutor(AbstractStep step, Server server, LogDispatcher logger) {
		this.step = step;
		this.server = server;
		if (logger != null)
			this.logger = logger;
		else
			this.logger = new ConsoleLogger();
	}

	protected boolean checkCanBeSkipped() throws StepExecutionException {
		try {
			server.getStepStateObserver().stepChecksNeedToRun(step);
			return step
					.canBeSkipped(server.getDataProxyProvider().getService());
		} catch (StepExecutionException e) {
			throw new StepExecutionException(e);
		}
	}

	protected void waitForReq() throws StepExecutionException {
		final DataProxy proxy = server.getDataProxyProvider().getService();
		server.getStepStateObserver().stepWaitForReq(step);
		synchronized (server) {
			try {
				while (!step.requirementsSatisfied(proxy)) {
					logger.debug(this, "requirements for step " + step
							+ " not satisfied, putting it to sleep");
					server.wait();
				}
				logger.debug(this, "requirements for step " + step
						+ " satisfied");
			} catch (InterruptedException e) {
				logger.error(this, e, e);
				server.notifyAll();
				throw new StepExecutionException(e);
			}
			server.notifyAll();
		}
	}

	protected boolean run() throws StepExecutionException {
		server.getStepStateObserver().stepStarted(step);
		System.out.println(this + ": running step " + step);
		// TODO remove try catch
		try {
			final StepProcessObserver listener = new StepProgressObserverImpl();
			final boolean success = step.run(server.getDataProxyProvider()
					.getService(), listener);
			logger.debug(this, "step " + step + " finished");
			synchronized (server) {
				server.notifyAll();
			}
			return success;
		} catch (Throwable t) {
			t.printStackTrace();
			synchronized (server) {
				server.notifyAll();
			}
			return false;
		}
	}
}
