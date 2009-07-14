package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.other.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

abstract class AbstractRunStateTraining extends AbstractStepProcessBuilder {

	private File resultFile;
	protected final File trainingFile, gtfFile, fastaFile;
	private final LogDispatcher logger;

	AbstractRunStateTraining(File conradWorkingDir, File workingDir,
			File trainingFile, File fastaFile, File gtfFile, LogDispatcher logger) {
		super(conradWorkingDir, workingDir);
		this.trainingFile = trainingFile;
		this.logger = logger;
		this.fastaFile = fastaFile;
		this.gtfFile = gtfFile;
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
		if(fastas == null || elements == null || logger == null)
			throw new NullPointerException("fastas="+fastas +", elements="+elements +", logger="+logger);
		final FASTAFile fastaFile = new FASTAFile(fastas);
		final GTFFile gtfFile = new GTFFile(elements);
		fastaFile.setLineLength(60);
		try {
			final File jetAnotherFile = new File(workingDir, ConradConstants.FASTA_FILE_NAME);
			final File andEvenOneMore = new File(workingDir, ConradConstants.GTF_FILE_NAME);
			logger.debug(this, "writing fastas to " + jetAnotherFile);
			fastaFile.writeToFile(jetAnotherFile);
			logger.debug(this, "writing gtf to " + andEvenOneMore);
			gtfFile.writeToFile(andEvenOneMore);
			jetAnotherFile.deleteOnExit();
			andEvenOneMore.deleteOnExit();
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
