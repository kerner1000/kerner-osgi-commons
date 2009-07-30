package de.mpg.mpiz.koeln.kerner.anna.step.conrad.predict;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.bioutils.gtf.GTFElement;
import de.bioutils.gtf.GTFFile;
import de.bioutils.gff.GFFFormatErrorException;

import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.common.StepExecutionException;

abstract class AbstractRunStatePredicting extends AbstractStepProcessBuilder {

	protected final File trainingFile;
	protected final File resultFile;
	private ArrayList<? extends GTFElement> result = null;

	protected AbstractRunStatePredicting(File workingDir, File executableDir,
			File trainingFile) {
		super(executableDir, workingDir);
		this.trainingFile = trainingFile;
		this.resultFile = new File(workingDir, "predict");
	}

	boolean run(File trainingFile) throws StepExecutionException {
		final File f = new File(resultFile.getAbsolutePath() + ".gtf");
		boolean success = true;
		try {
			if (f.exists() && f.canRead()) {
				System.out.println(this + ": file " + f
						+ " exists, taking shortcut");
				finish(f);
				return success;
			}
			success = createAndStartProcess();
			if (success) {
				System.out
						.println(this
								+ ": predicting succeeded, expecting prediction file at "
								+ f);
				finish(f);
			}
			return success;
		} catch (IOException e) {
			throw new StepExecutionException(e);
		} catch (GFFFormatErrorException e) {
			throw new StepExecutionException(e);

		}
	}

	private void finish(File f) throws IOException, GFFFormatErrorException {
		System.out.println(this + ": reading " + f);
		final GTFFile gtfFile = new GTFFile(f, null);
		result = (ArrayList<? extends GTFElement>) gtfFile.getElements();
		System.out.println(this + ": got GTF from " + f + ", fist entry: "
				+ result.get(0));
	}

	public ArrayList<? extends GTFElement> getResult() {
		return result;
	}
}
