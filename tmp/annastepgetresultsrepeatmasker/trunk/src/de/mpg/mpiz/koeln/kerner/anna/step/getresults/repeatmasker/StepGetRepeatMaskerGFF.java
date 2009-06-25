package de.mpg.mpiz.koeln.kerner.anna.step.getresults.repeatmasker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxyProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

public class StepGetRepeatMaskerGFF extends AbstractStep {
	
	private final static String OUT_DIR_KEY = "anna.step.getResults.outDir";
	private final static String OUT_FILE_NAME_KEY = "anna.step.getResults.repeatMasker.fileName";

	public String toString(){
		return this.getClass().getSimpleName(); 
	}
	
	@Override
	public boolean canBeSkipped(DataProxyProvider data)
			throws StepExecutionException {
		return false;
	}

	@Override
	public boolean requirementsSatisfied(DataProxyProvider data)
			throws StepExecutionException {
		try {
			final ArrayList<? extends GTFElement> elements = data
					.getDataProxy().getRepeatMaskerGtf();
			// TODO predicted genes may be size==0
			return (elements != null && elements.size() != 0);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean run(DataProxyProvider data, StepProcessObserver listener)
			throws StepExecutionException {
		boolean success = false;
		try {
			final File outDir = new File(super.getStepProperties().getProperty(
					OUT_DIR_KEY));
			final File outFile = new File(outDir, super.getStepProperties()
					.getProperty(OUT_FILE_NAME_KEY));
			success = checkOutDir(outDir);
			if (success) {
				System.out.println(this + ": writing repeatmasker gff to "
						+ outFile);
				final GTFFile file = new GTFFile(data.getDataProxy()
						.getRepeatMaskerGtf());
				file.writeToFile(outFile);
			}
		} catch (Throwable t){t.printStackTrace(); System.exit(1);}
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

}
