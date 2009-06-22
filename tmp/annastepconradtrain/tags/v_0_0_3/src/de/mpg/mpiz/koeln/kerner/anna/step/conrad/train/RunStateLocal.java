package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

class RunStateLocal extends AbstractRunStateTraining {

	RunStateLocal(File workingDir, File conradWorkingDir,
			String trainingFileName) {
		super(workingDir, conradWorkingDir, trainingFileName);
	}

	@Override
	protected
	List<String> getCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(new File(conradWorkingDir,ConradConstants.CONRAD_EXE).getAbsolutePath());
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(stepWorkingDir.getAbsolutePath());
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		return builder.getCommandList();
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}
}
