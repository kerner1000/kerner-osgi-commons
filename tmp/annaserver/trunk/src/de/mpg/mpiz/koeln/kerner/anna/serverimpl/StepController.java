package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanProvider;

class StepController implements Callable<Boolean> {

	private final AbstractStepExecutor exe;

	StepController(AbstractStep step, DataBeanProvider provider) {
		System.out.println(this + ": running step " + step);
		exe = new LocalSepExecutor(step, provider);
	}

	public Boolean call() throws Exception {
		// call "call()" directly to run in same thread
		exe.call();
		return true;
	}

}
