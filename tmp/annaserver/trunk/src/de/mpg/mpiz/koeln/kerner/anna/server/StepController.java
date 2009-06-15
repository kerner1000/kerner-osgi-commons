package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.concurrent.Callable;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;

class StepController implements Callable<Boolean> {

	private final AbstractStepExecutor exe;

	StepController(AbstractStep step, ServerActivator serverActivator) {
		boolean lsf = checkIfLSF(step);
		if(lsf){
			ServerActivator.LOGGER.debug(this, "step " + step + " wants to run on LSF");
			exe = new LSFExecutor(step, serverActivator);
		}
		else {
			ServerActivator.LOGGER.debug(this, "running step " + step + " locally");
			exe = new LocalSepExecutor(step, serverActivator);
		}
	}

	private boolean checkIfLSF(AbstractStep step2) {
		return step2.getStepProperties().getProperty(AbstractStep.KEY_ENV).equals(AbstractStep.VALUE_ENV_LSF);
	}

	public Boolean call() throws Exception {
		// call "call()" directly to run in same thread
		exe.call();
		return true;
	}

	
}
