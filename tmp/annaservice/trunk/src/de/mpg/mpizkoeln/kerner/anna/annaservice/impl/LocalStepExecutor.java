package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBean;

class LocalStepExecutor extends AbstractStepExecutor {

	LocalStepExecutor(AbstractStep step, DataBean data) {
		super(step, data);
	}

	public Void call() throws Exception {
		step.run(data);
		return null;
	}

}
