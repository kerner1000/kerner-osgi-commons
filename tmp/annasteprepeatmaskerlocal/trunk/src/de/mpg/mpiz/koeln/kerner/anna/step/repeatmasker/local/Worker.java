package de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.local;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;

class Worker extends AbstractStepProcessBuilder {

	private final static String EXE = "RepeatMasker";
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
		// ./RepeatMasker -pa 2 -s -gff
		// /home/proj/kerner/diplom/conrad/trainingAndCrossValidationWithProvidedData/test3/ref.fasta
		final CommandStringBuilder builder = new CommandStringBuilder(new File(
				executableDir, EXE).getAbsolutePath());
//		builder.addValueCommand("-pa", "2");
		builder.addAllFlagCommands("s");
		builder.addFlagCommand("-gff");
		builder.addFlagCommand(inFile.getAbsolutePath());
		return builder.getCommandList();
	}
}
