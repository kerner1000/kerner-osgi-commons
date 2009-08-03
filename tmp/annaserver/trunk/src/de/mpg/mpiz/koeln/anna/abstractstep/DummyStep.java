package de.mpg.mpiz.koeln.anna.abstractstep;

import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;

class DummyStep extends AbstractStep {
	
	private final String name;
	
	DummyStep(String name){
		this.name = name;
	}

	@Override
	public boolean canBeSkipped(DataProxy data) throws StepExecutionException {
		return true;
	}

	@Override
	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		return true;
	}

	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		return true;
	}
	
	@Override
	public String toString() {
		return "(dummy)"+name;
	}

}
