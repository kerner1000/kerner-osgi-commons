package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

abstract class AbstractRunStateTraining extends AbstractStepProcessBuilder {

	private File resultFile;
	protected final File trainingFile, gtfFile, fastaFile;

	AbstractRunStateTraining(File conradWorkingDir, File workingDir,
			File trainingFile) {
		super(conradWorkingDir, workingDir);
		this.trainingFile = trainingFile;
		fastaFile = new File(workingDir, StepConradTrain.PROPERTIES_KEY_PREFIX
				+ ConradConstants.FASTA_FILE_NAME);
		gtfFile = new File(workingDir, StepConradTrain.PROPERTIES_KEY_PREFIX
				+ ConradConstants.GTF_FILE_NAME);
	}

	boolean run(ArrayList<? extends FASTASequence> fastas,
			ArrayList<? extends GTFElement> elements)
			throws StepExecutionException {
		if (!writeDataToDir(fastas, elements)) {
			return false;
		}
		final boolean b = createAndStartProcess();
		if (b)
			return assignResult();
		return false;
	}

	private boolean assignResult() {
		final File result = new File(workingDir, trainingFile.getName());
		if (!result.exists() || !result.canRead()) {
			logger
					.warn(this,
							"process execution was sucessfull, but training file is not accessible");
			return false;
		}
		this.resultFile = result;
		return true;
	}

	private boolean writeDataToDir(ArrayList<? extends FASTASequence> fastas,
			ArrayList<? extends GTFElement> elements) throws StepExecutionException {
		final FASTAFile fastaFile = new FASTAFile(fastas);
		final GTFFile gtfFile = new GTFFile(elements);
		fastaFile.setLineLength(60);
		try {
			logger.debug(this, "writing fastas to " + this.fastaFile);
			fastaFile.writeToFile(this.fastaFile);
			logger.debug(this, "writing gtf to " + this.gtfFile);
			gtfFile.writeToFile(this.gtfFile);
			return true;
		} catch (IOException e) {
			logger.error(this, e.toString(), e);
			throw new StepExecutionException(e);
		}
	}

	File getResult() {
		return resultFile;
	}

}
