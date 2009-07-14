package de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common;

import java.io.File;

import org.osgi.framework.BundleContext;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.abstractstep.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;

public abstract class AbstractStepRepeatMasker extends AbstractStep {
	
	protected LogDispatcher logger;
	protected File exeDir;
	protected File workingDir;

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected synchronized void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		logger = new LogDispatcherImpl(context);
		assignProperties();
		validateProperties();
		printProperties();
	}

	private void assignProperties() {
		exeDir = new File(getStepProperties().getProperty(RepeatMaskerConstants.EXE_DIR_KEY));
		workingDir = new File(getStepProperties().getProperty(RepeatMaskerConstants.WORKING_DIR_KEY));
	}

	private void validateProperties() throws StepExecutionException {
		if (!FileUtils.dirCheck(exeDir, false))
			throw new StepExecutionException(
					"cannot access repeatmasker working dir");
		if (!FileUtils.dirCheck(workingDir, true))
			throw new StepExecutionException("cannot access step working dir");
	}

	private void printProperties() {
		logger.debug(this, " created, properties:");
		logger.debug(this, "\tstepWorkingDir=" + workingDir);
		logger.debug(this, "\texeDir=" + exeDir);
	}

	@Override
	public boolean canBeSkipped(DataProxy data)
			throws StepExecutionException {
		try {
			// must this two actions be atomar?
			final boolean repeatGtf = (data.getRepeatMaskerGtf() != null);
			final boolean repeatGtfSize = (data
					.getRepeatMaskerGtf().size() != 0);
			
			return (repeatGtf && repeatGtfSize);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			// must this two actions be atomar?
			final boolean sequence = (data.getInputSequences() != null);
			final boolean sequenceSize = (data
					.getInputSequences().size() != 0);
			
			return (sequence && sequenceSize);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

}
