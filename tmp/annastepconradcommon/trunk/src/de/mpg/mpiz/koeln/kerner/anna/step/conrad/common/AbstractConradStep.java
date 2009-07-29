package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;
import java.io.FileNotFoundException;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.abstractstep.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 *
 */
public abstract class AbstractConradStep extends AbstractStep {
	
	protected final static String WORKING_DIR_KEY = ConradConstants.PROPERTIES_KEY_PREFIX
	+ "workingDir";
	
	protected LogDispatcher logger;
	protected AbstractStepProcessBuilder process;
	protected File exeDir;
	protected File workingDir;
	
	protected void init() throws Exception {
		exeDir = new File(super.getStepProperties()
				.getProperty(ConradConstants.CONRAD_DIR_KEY));
		workingDir = new File(super.getStepProperties().getProperty(
				WORKING_DIR_KEY));
		if (!FileUtils.dirCheck(workingDir.getAbsoluteFile(), true))
			throw new FileNotFoundException("cannot access working dir "
					+ workingDir.getAbsolutePath());
		process = getProcess();
	}
	
	protected abstract AbstractStepProcessBuilder getProcess();
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
