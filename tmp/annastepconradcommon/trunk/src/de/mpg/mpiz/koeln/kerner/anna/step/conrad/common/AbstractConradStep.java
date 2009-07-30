package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;
import java.io.FileNotFoundException;

import org.osgi.framework.BundleContext;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.abstractstep.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepUtils;

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
	
	protected synchronized void init(BundleContext context) throws StepExecutionException {
		logger = new LogDispatcherImpl(context);
		try{
		exeDir = new File(super.getStepProperties()
				.getProperty(ConradConstants.CONRAD_DIR_KEY));
		logger.debug(this, "got exe dir="+exeDir);
		workingDir = new File(super.getStepProperties().getProperty(
				WORKING_DIR_KEY));
		logger.debug(this, "got working dir="+workingDir.getAbsolutePath());
		if (!FileUtils.dirCheck(workingDir.getAbsoluteFile(), true))
			throw new FileNotFoundException("cannot access working dir "
					+ workingDir.getAbsolutePath());
		process = getProcess();
		}catch(Exception e){
			StepUtils.handleStepException(this, e, logger);
		}
	}
	
	protected abstract AbstractStepProcessBuilder getProcess();
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
