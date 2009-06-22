package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.AbstractRunState;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

abstract class AbstractRunStateTraining extends AbstractRunState {

	private File result = null;
	protected final File trainingFile, gtfFile, fastaFile;
	protected final String trainingFileName;

	AbstractRunStateTraining(File stepWorkingDir, File conradWorkingDir,
			String trainingFileName) {
		super(stepWorkingDir, conradWorkingDir);
		this.trainingFileName = trainingFileName;
		trainingFile = new File(stepWorkingDir, trainingFileName);
		fastaFile = new File(stepWorkingDir, ConradConstants.FASTA_FILE_NAME);
		gtfFile = new File(stepWorkingDir, ConradConstants.GTF_FILE_NAME);
	}

	boolean run(ArrayList<? extends FASTASequence> fastas,
			ArrayList<? extends GTFElement> elements)
			throws StepExecutionException {
		if (filesAlreadyThere()) {
			System.out.println(this
					+ " all files already there, no need to run");
			assignResult();
			return true;
		}
		if (!writeDataToDir(fastas, elements)) {
			return false;
		}
		final boolean b = createAndStartProcess();
		if (b) {
			assignResult();
		}
		return b;
	}
	
	private void assignResult(){
		final File result = new File(stepWorkingDir, trainingFileName);
		this.result = result;
	}

	private boolean filesAlreadyThere() {
		return (gtfFile.exists() && gtfFile.canRead() && fastaFile.exists()
				&& fastaFile.canRead() && trainingFile.exists() && trainingFile
				.canRead());
	}

	private boolean writeDataToDir(ArrayList<? extends FASTASequence> fastas,
			ArrayList<? extends GTFElement> elements) {
		final FASTAFile fastaFile = new FASTAFile(fastas);
		final GTFFile gtfFile = new GTFFile(elements);
		fastaFile.setLineLength(60);
		try {
			System.out.println(this + " writing fastas to " + this.fastaFile);
			fastaFile.writeToFile(this.fastaFile);
			System.out.println(this + " writing gtf to " + this.gtfFile);
			gtfFile.writeToFile(this.gtfFile);
			return true;
		} catch (IOException e) {
			System.out.println(this + ": error while creating files: "
					+ this.fastaFile + ", " + this.gtfFile);
			return false;
		}
	}

	File getResult() {
		return result;
	}

}
