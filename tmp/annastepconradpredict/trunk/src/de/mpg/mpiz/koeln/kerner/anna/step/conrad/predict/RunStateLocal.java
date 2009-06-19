package de.mpg.mpiz.koeln.kerner.anna.step.conrad.predict;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

class RunStateLocal extends AbstractRunStatePredicting {

	protected RunStateLocal(File stepWorkingDir, File conradWorkingDir,
			File trainingFile) {
		super(stepWorkingDir, conradWorkingDir, trainingFile);
	}

	@Override
	protected List<String> getCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(new File(conradWorkingDir,ConradConstants.CONRAD_EXE).getAbsolutePath());
		builder.addFlagCommand("predict");
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		builder.addFlagCommand(stepWorkingDir.getAbsolutePath());
		builder.addFlagCommand("predict");
		return builder.getCommandList();
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
