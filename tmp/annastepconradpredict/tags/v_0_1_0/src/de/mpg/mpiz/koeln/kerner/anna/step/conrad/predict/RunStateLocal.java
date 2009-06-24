package de.mpg.mpiz.koeln.kerner.anna.step.conrad.predict;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

class RunStateLocal extends AbstractRunStatePredicting {

	protected RunStateLocal(File workingDir, File executableDir,
			File trainingFile) {
		super(workingDir, executableDir, trainingFile);
	}

	@Override
	protected List<String> getCommandList() {
		// bin/conrad.sh predict <training file> <input data> <base filename for
		// output >
		final CommandStringBuilder builder = new CommandStringBuilder(new File(
				executableDir, ConradConstants.CONRAD_EXE).getAbsolutePath());
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
