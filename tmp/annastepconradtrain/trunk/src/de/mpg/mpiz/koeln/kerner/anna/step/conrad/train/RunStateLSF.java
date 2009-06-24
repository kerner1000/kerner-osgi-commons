package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

class RunStateLSF extends AbstractRunStateTraining {
	
	RunStateLSF(File conradWorkingDir, File workingDir, File trainingFile,
			File fastaFile, File gtfFile, LogDispatcher logger) {
		super(conradWorkingDir, workingDir, trainingFile, fastaFile, gtfFile, logger);
		
	}

	@Override
	protected
	List<String> getCommandList() {
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
	
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
