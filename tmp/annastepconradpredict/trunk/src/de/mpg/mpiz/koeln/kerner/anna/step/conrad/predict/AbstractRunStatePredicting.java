package de.mpg.mpiz.koeln.kerner.anna.step.conrad.predict;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.step.common.AbstractStepProcessBuilder;

abstract class AbstractRunStatePredicting extends AbstractStepProcessBuilder {

	protected final File trainingFile;
	protected final File resultFile;
	private ArrayList<? extends GTFElement> result = null;

	protected AbstractRunStatePredicting(File workingDir,
			File executableDir, File trainingFile) {
		super(executableDir, workingDir);
		this.trainingFile = trainingFile;
		this.resultFile = new File(workingDir, "predict");
	}
	
	boolean run(File trainingFile) throws StepExecutionException{
		final boolean b = createAndStartProcess();
		if (b) {
			try {
				final File f = new File(resultFile.getAbsolutePath() + ".gtf");
				System.out.println(this + ": predicting finished, expecting prediction file at " + f);
				System.out.println(this + ": reading " + f);
				GTFFile gtfFile = new GTFFile(f);
				result = gtfFile.getElements();
				System.out.println(this + ": got GTF from " + f + ", fist entry: " + result.get(0));
			} catch (IOException e) {
				throw new StepExecutionException(e);
			} catch (GTFFormatErrorException e) {
				throw new StepExecutionException(e);
			}
		}
		return b;
	}

	public ArrayList<? extends GTFElement> getResult() {
		return result;
	}
}
