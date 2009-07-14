package de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.local;

import java.io.File;
import java.io.IOException;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

import de.mpg.mpiz.koeln.kerner.anna.other.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common.AbstractStepRepeatMasker;
import de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common.RepeatMaskerConstants;

public class StepRepeatMaskerLocal extends AbstractStepRepeatMasker {
	
	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		final File inFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME);
		final File outFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME
				+ RepeatMaskerConstants.OUTFILE_POSTFIX);
		final AbstractStepProcessBuilder worker = new Worker(workingDir,
				exeDir, logger, inFile);
		boolean success = true;
		try{
			new FASTAFile(data.getInputSequences())
			.writeToFile(inFile);
		worker.addTempInFile(inFile);
		worker.addResultFile(true, outFile);
		success = worker.createAndStartProcess();
		if (success) {
			upUpdate(data, outFile);
		}
		}catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
			return false;
		}
		return success;
	}
/**
	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		final File infile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME);
		infile.deleteOnExit();
		final File outFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME
				+ RepeatMaskerConstants.OUTFILE_POSTFIX);
		try {
			if(FileUtils.fileCheck(outFile, false)){
				logger.debug(this, "repeatmasker output already there, taking shortcut ("
						+ outFile + ")");
				upUpdate(data, outFile);
				return true;
			}
			new FASTAFile(data.getInputSequences())
					.writeToFile(infile);
			logger.debug(this, "created temp file with input sequence(s): "
					+ infile);
			final AbstractStepProcessBuilder worker = new Worker(workingDir,
					exeDir, logger, infile);
			final boolean success = worker.createAndStartProcess();
			if (success) {
				upUpdate(data, outFile);
			}
			return success;
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
			return false;
		}
	}
	
	*/
	
	private void upUpdate(DataProxy data, File outFile) throws DataBeanAccessException, IOException, GTFFormatErrorException{
		data.setRepeatMaskerGtf(
				new GTFFile(outFile).getElements());
	}
}
