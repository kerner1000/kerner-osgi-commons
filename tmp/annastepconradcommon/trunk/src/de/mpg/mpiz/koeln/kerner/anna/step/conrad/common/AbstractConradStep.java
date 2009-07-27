package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.abstractstep.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;

public abstract class AbstractConradStep extends AbstractStep {
	
	protected final static String WORKING_DIR_KEY = ConradConstants.PROPERTIES_KEY_PREFIX
	+ "workingDir";
	
	protected LogDispatcher logger;
	protected AbstractStepProcessBuilder process;
	protected File workingDir;
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
