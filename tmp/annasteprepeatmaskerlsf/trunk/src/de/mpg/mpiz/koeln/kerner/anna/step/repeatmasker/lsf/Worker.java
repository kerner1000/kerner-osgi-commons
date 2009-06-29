package de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.lsf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common.RepeatMaskerConstants;

class Worker extends AbstractStepProcessBuilder {
	
	private final File inFile;

	protected Worker(File stepWorkingDir, File executableDir,
			LogDispatcher logger, File inFile) {
		super(stepWorkingDir, executableDir, logger);
		this.inFile = inFile;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected List<String> getCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(LSF.BSUB_EXE);
		builder.addAllFlagCommands(LSF.getBsubFlagCommandStrings());
		builder.addAllValueCommands(LSF.getBsubValueCommandStrings(workingDir));
		builder.addFlagCommand(new File(
				executableDir, RepeatMaskerConstants.EXE).getAbsolutePath());
//		builder.addAllFlagCommands("-s");
		builder.addFlagCommand("-gff");
		builder.addFlagCommand(inFile.getAbsolutePath());
		return builder.getCommandList();
	}

}
