package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;

/**
 * 
 * Actually, this class just hands over step (and server) to LocalStepExecutor
 *
 */
class StepController implements Callable<Void> {

	private final AbstractStepExecutor exe;

	StepController(AbstractStep step, Server server, LogDispatcher logger) {
		exe = new LocalSepExecutor(step, server, logger);
	}

	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ exe.getClass().getSimpleName();
	}

	public Void call() throws Exception {
		// call "call()" directly to run in same thread
		exe.call();
		return null;
	}

}
