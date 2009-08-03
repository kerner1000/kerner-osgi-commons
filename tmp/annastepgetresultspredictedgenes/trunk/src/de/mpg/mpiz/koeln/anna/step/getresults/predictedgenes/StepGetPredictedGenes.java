package de.mpg.mpiz.koeln.anna.step.getresults.predictedgenes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.bioutils.gtf.GTFElement;
import de.bioutils.gtf.GTFFile;

import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;

public class StepGetPredictedGenes extends AbstractStep {

	private final static String OUT_DIR_KEY = "anna.step.getResults.outDir";
	private final static String OUT_FILE_NAME_KEY = "anna.step.getResults.predictedGenes.fileName";

	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			final ArrayList<? extends GTFElement> elements = data
					.getPredictedGenesGtf();
			// TODO predicted genes may be size==0
			return (elements != null && elements.size() != 0);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

	public boolean canBeSkipped(DataProxy data)
			throws StepExecutionException {
		return false;
	}

	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		boolean success = false;
		try {
			final File outDir = new File(super.getStepProperties().getProperty(
					OUT_DIR_KEY));
			final File outFile = new File(outDir, super.getStepProperties()
					.getProperty(OUT_FILE_NAME_KEY));
			success = checkOutDir(outDir);
			if (success) {
				System.out.println(this + ": writing predicted genes to "
						+ outFile);
				final GTFFile file = new GTFFile(data
						.getPredictedGenesGtf());
				file.write(outFile);
			}
		} catch (DataBeanAccessException e) {

		} catch (IOException e) {

		}
		return success;
	}

	private boolean checkOutDir(File outFile) {
		if (!outFile.exists()) {
			System.out.println(this + ": " + outFile
					+ " does not exist, creating");
			final boolean b = outFile.mkdirs();
			return b;
		}
		return outFile.canWrite();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
