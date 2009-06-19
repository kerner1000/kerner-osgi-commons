package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;

class StepController implements Callable<Boolean> {

	private final AbstractStepExecutor exe;

	StepController(AbstractStep step, Server server) {
		exe = new LocalSepExecutor(step, server);
	}
	
	public String toString() {
		return this.getClass().getSimpleName() + ":" + exe.getClass().getSimpleName();
	}

	public Boolean call() throws Exception {
		System.out.println(this + ": stepexecutor activated " + exe);
		// call "call()" directly to run in same thread
		exe.call();
		return true;
	}

}
