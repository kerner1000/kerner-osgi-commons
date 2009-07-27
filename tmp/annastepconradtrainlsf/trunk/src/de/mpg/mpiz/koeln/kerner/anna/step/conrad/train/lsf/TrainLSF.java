package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train.lsf;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.AbstractConradTrainStep;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

public class TrainLSF extends AbstractConradTrainStep {

	private class Process extends AbstractStepProcessBuilder {

		private final File trainingFile;

		protected Process(File executableDir, File workingDir,
				File trainingFile, LogDispatcher logger) {
			super(executableDir, workingDir, logger);
			this.trainingFile = trainingFile;
			logger.debug(this, "executableDir="+executableDir);
			logger.debug(this, "workingDir="+workingDir);
			logger.debug(this, "executableDir="+executableDir);			
		}

		@Override
		protected List<String> getCommandList() {
			final CommandStringBuilder builder = new CommandStringBuilder(LSF.BSUB_EXE);
			builder.addAllFlagCommands(LSF.getBsubFlagCommandStrings());
			builder.addAllValueCommands(LSF.getBsubValueCommandStrings(workingDir));
			builder.addFlagCommand(ConradConstants.CONRAD_EXE);
			builder.addFlagCommand("train");
			builder.addFlagCommand("models/singleSpecies.xml");
			builder.addFlagCommand(workingDir.getAbsolutePath());
			builder.addFlagCommand(trainingFile.getAbsolutePath());
			return builder.getCommandList();
		}

	}
	
	@Override
	protected AbstractStepProcessBuilder getProcess(File executableDir, File workingDir,
			File trainingFile) {
		return new Process(executableDir, workingDir, trainingFile, logger);
	}

}
