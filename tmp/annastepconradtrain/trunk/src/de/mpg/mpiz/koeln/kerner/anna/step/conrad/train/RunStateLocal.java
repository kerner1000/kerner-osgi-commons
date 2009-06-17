package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;

class RunStateLocal extends AbstractRunState {

	@Override
	List<String> getCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(new File(CONRAD_DIR,CONRAD_EXE).getAbsolutePath());
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(WORKING_DIR.getAbsolutePath());
		final File trainingFile = new File(WORKING_DIR, TRAINING_FILE_NAME);
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		return builder.getCommandList();
	}
}
