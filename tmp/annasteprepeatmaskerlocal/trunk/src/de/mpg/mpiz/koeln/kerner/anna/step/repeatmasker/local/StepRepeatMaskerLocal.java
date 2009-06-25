package de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.local;

import java.io.File;
import java.io.IOException;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxyProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common.AbstractStepRepeatMasker;
import de.mpg.mpiz.koeln.kerner.anna.step.repeatmasker.common.RepeatMaskerConstants;

public class StepRepeatMaskerLocal extends AbstractStepRepeatMasker {

	@Override
	public boolean run(DataProxyProvider data, StepProcessObserver listener)
			throws StepExecutionException {
		final File infile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME);
		infile.deleteOnExit();
		final File outFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME
				+ RepeatMaskerConstants.OUTFILE_POSTFIX);
		try {
			if(outFile.exists() && outFile.canRead()){
				logger.debug(this, "repeatmasker output already there, taking shortcut ("
						+ outFile + ")");
				upUpdate(data, outFile);
				return true;
			}
			new FASTAFile(data.getDataProxy().getInputSequences())
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
	
	private void upUpdate(DataProxyProvider data, File outFile) throws DataBeanAccessException, IOException, GTFFormatErrorException{
		data.getDataProxy().setRepeatMaskerGtf(
				new GTFFile(outFile).getElements());
	}
}
