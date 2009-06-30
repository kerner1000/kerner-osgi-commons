package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;

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
