package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.concurrent.Callable;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBean;

abstract class AbstractStepExecutor implements Callable<Void>{
	
	protected final AbstractStep step;
	protected final DataBean data;
	
	AbstractStepExecutor(AbstractStep step, DataBean data) {
		this.step = step;
		this.data = data;
	}
}
