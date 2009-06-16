package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBeanProvider;

class LSFExecutor extends AbstractStepExecutor {

	LSFExecutor(AbstractStep step, DataBeanProvider provider) {
		super(step, provider);
	}

	public Boolean call() throws Exception {
		throw new RuntimeException("Not implemented jet");
	}
}
