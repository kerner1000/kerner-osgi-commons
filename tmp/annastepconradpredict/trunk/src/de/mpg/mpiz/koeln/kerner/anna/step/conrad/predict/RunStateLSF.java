package de.mpg.mpiz.koeln.kerner.anna.step.conrad.predict;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

class RunStateLSF extends AbstractRunStatePredicting {

	protected RunStateLSF(File workingDir, File executableDir, File trainingFile) {
		super(workingDir, executableDir, trainingFile);
	}

	@Override
	protected List<String> getCommandList() {
		// bin/conrad.sh predict <training file> <input data> <base filename for
		// output >
		final CommandStringBuilder builder = new CommandStringBuilder(LSF.BSUB_EXE);
		builder.addAllFlagCommands(LSF.getBsubFlagCommandStrings());
		builder.addAllValueCommands(LSF.getBsubValueCommandStrings(workingDir));
		builder.addFlagCommand(ConradConstants.CONRAD_EXE);
		builder.addFlagCommand("predict");
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		builder.addFlagCommand(workingDir.getAbsolutePath());
		builder.addFlagCommand(resultFile.getAbsolutePath());
		return builder.getCommandList();
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
