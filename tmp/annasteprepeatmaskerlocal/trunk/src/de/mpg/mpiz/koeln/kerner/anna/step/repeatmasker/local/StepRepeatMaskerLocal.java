package de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.bioutils.fasta.FASTAFile;
import de.bioutils.fasta.FASTAFileImpl;
import de.bioutils.fasta.FASTASequence;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gtf.GTFElement;
import de.bioutils.gtf.GTFFile;
import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.other.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common.AbstractStepRepeatMasker;
import de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common.RepeatMaskerConstants;

public class StepRepeatMaskerLocal extends AbstractStepRepeatMasker {
	
	private class Process extends AbstractStepProcessBuilder {

		private final File inFile;

		protected Process(File executableDir, File stepWorkingDir,
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
					executableDir, RepeatMaskerConstants.EXE).getAbsolutePath());
//			builder.addValueCommand("-pa", "2");
//			builder.addAllFlagCommands("-s");
			builder.addFlagCommand("-gff");
			builder.addFlagCommand("-qq");
			builder.addFlagCommand(inFile.getAbsolutePath());
			return builder.getCommandList();
		}
	}
	
	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		final File inFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME);
		final File outFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME
				+ RepeatMaskerConstants.OUTFILE_POSTFIX);
		final AbstractStepProcessBuilder worker = new Process(exeDir, workingDir
				, logger, inFile);
		boolean success = true;
		try{
			new FASTAFileImpl(data.getInputSequences())
			.write(inFile);
		success = worker.createAndStartProcess();
		if (success) {
			upUpdate(data, outFile);
		}
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(e);
		}
		return success;
	}
	
	private void upUpdate(DataProxy data, File outFile) throws DataBeanAccessException, IOException, GFFFormatErrorException{
		data.setRepeatMaskerGtf(
				(ArrayList<? extends GTFElement>) new GTFFile(outFile, null).getElements());
	}
}
