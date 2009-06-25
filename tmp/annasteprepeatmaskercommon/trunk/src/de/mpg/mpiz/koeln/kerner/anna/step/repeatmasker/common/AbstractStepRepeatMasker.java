package de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common;

import java.io.File;

import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxyProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

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
		if (!exeDir.exists() || !exeDir.canRead())
			throw new StepExecutionException(
					"cannot access repeatmasker working dir");
		if (!checkWorkingDir(workingDir))
			throw new StepExecutionException("cannot access step working dir");
	}

	private void printProperties() {
		logger.debug(this, " created, properties:");
		logger.debug(this, "\tstepWorkingDir=" + workingDir);
		logger.debug(this, "\texeDir=" + exeDir);
	}

	private boolean checkWorkingDir(final File workingDir) {
		if (!workingDir.exists()) {
			System.out.println(this + ": " + workingDir
					+ " does not exist, creating");
			final boolean b = workingDir.mkdirs();
			return b;
		}
		return workingDir.canWrite();
	}

	@Override
	public boolean canBeSkipped(DataProxyProvider data)
			throws StepExecutionException {
		try {
			final boolean repeatGtf = (data.getDataProxy().getRepeatMaskerGtf() != null);
			final boolean repeatGtfSize = (data.getDataProxy()
					.getRepeatMaskerGtf().size() != 0);
			return (repeatGtf && repeatGtfSize);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean requirementsSatisfied(DataProxyProvider data)
			throws StepExecutionException {
		try {
			final boolean sequence = (data.getDataProxy().getInputSequences() != null);
			final boolean sequenceSize = (data.getDataProxy()
					.getInputSequences().size() != 0);
			return (sequence && sequenceSize);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

}
