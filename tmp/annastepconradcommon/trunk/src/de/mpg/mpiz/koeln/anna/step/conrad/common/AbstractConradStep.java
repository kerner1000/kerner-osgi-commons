package de.mpg.mpiz.koeln.anna.step.conrad.common;

import java.io.File;

import org.osgi.framework.BundleContext;

import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 *
 */
public abstract class AbstractConradStep extends AbstractStep {
	
	protected AbstractStepProcessBuilder process;
	protected File exeDir;
	protected File workingDir;
	
	protected synchronized void init(BundleContext context) throws StepExecutionException {
		try{
			super.init(context);
		exeDir = new File(super.getStepProperties()
				.getProperty(ConradConstants.CONRAD_DIR_KEY));
		logger.debug(this, "got exe dir="+exeDir);
		}catch(Exception e){
			StepUtils.handleException(this, e, logger);
		}
	}
	
	protected abstract AbstractStepProcessBuilder getProcess();
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
