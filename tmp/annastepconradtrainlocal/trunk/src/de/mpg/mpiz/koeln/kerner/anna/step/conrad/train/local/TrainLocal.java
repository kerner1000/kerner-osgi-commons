package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train.local;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.AbstractConradTrainStep;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

public class TrainLocal extends AbstractConradTrainStep {

	private class Process extends AbstractStepProcessBuilder {

		private final File trainingFile;

		protected Process(File executableDir, File workingDir,
				File trainingFile, LogDispatcher logger) {
			super(executableDir, workingDir , logger);
			this.trainingFile = trainingFile;
			// TODO WAS IS DAS HIER WIEDER FÜR EIN AFFENSCHEISS !?!?!
			try{
				System.out.println("executableDir="+executableDir);
				System.out.println("workingDir="+workingDir);
				System.out.println("executableDir="+executableDir);
			logger.debug(this, "executableDir="+executableDir);
			logger.debug(this, "workingDir="+workingDir);
			logger.debug(this, "executableDir="+executableDir);
			}catch(Throwable t){
				t.printStackTrace();
			}
		}

		@Override
		protected List<String> getCommandList() {
			final CommandStringBuilder builder = new CommandStringBuilder(
					new File(executableDir, ConradConstants.CONRAD_EXE)
							.getAbsolutePath());
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
