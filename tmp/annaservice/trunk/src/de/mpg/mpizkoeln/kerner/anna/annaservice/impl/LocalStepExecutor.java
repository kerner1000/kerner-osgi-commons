package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;

class LocalStepExecutor extends AbstractStepExecutor {

	LocalStepExecutor(AbstractStep step) {
		super(step);
	}

	public Void call() throws Exception {
		step.run();
		return null;
	}

}
