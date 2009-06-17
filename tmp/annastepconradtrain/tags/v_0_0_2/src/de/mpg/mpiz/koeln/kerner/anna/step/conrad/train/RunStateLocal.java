package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;

class RunStateLocal extends AbstractRunState {

	RunStateLocal(File workingDir, File conradWorkingDir,
			String trainingFileName) {
		super(workingDir, conradWorkingDir, trainingFileName);
	}

	@Override
	List<String> getCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(new File(conradWorkingDir,CONRAD_EXE).getAbsolutePath());
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(workingDir.getAbsolutePath());
		final File trainingFile = new File(workingDir, trainingFileName);
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		return builder.getCommandList();
	}
}
