package de.mpg.mpiz.koeln.kerner.anna.step.conrad.predict;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.AbstractRunState;

abstract class AbstractRunStatePredicting extends AbstractRunState {

	protected final File trainingFile;
	private ArrayList<? extends GTFElement> result = null;

	protected AbstractRunStatePredicting(File stepWorkingDir,
			File conradWorkingDir, File trainingFile) {
		super(stepWorkingDir, conradWorkingDir);
		this.trainingFile = trainingFile;
	}
	
	boolean run(File trainingFile) throws StepExecutionException{
		final boolean b = createAndStartProcess();
		if (b) {
			try {
				GTFFile gtfFile = new GTFFile(trainingFile);
				this.result = gtfFile.getElements();
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
