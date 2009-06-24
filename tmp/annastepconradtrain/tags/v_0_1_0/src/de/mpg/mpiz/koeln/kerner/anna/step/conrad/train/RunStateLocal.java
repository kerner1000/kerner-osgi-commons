package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

class RunStateLocal extends AbstractRunStateTraining {

	RunStateLocal(File conradWorkingDir, File workingDir, File trainingFile,
			File fastaFile, File gtfFile, LogDispatcher logger) {
		super(conradWorkingDir, workingDir, trainingFile, fastaFile, gtfFile, logger);
	}

	@Override
	protected
	List<String> getCommandList() {
//		bin/conrad.sh train <xml model file> <training data> <training file>
		final CommandStringBuilder builder = new CommandStringBuilder(new File(executableDir,ConradConstants.CONRAD_EXE).getAbsolutePath());
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(workingDir.getAbsolutePath());
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		return builder.getCommandList();
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}
}
