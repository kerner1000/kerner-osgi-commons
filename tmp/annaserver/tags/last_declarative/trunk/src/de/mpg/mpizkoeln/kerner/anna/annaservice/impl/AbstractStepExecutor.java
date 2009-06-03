package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.concurrent.Callable;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;

abstract class AbstractStepExecutor implements Callable<Void>{
	
	protected final AbstractStep step;
	
	AbstractStepExecutor(AbstractStep step) {
		this.step = step;
	}
}
