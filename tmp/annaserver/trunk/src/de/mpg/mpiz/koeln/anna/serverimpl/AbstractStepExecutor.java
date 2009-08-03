package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;

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
}
