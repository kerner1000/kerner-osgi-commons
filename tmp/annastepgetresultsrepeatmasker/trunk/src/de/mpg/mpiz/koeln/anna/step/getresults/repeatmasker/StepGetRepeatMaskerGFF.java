package de.mpg.mpiz.koeln.anna.step.getresults.repeatmasker;

import java.io.File;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;

import de.bioutils.gff.GFFElement;
import de.bioutils.gff.GFFFile;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;

public class StepGetRepeatMaskerGFF extends AbstractStep {
	
	private final static String OUT_DIR_KEY = "anna.step.getResults.outDir";
	private final static String OUT_FILE_NAME_KEY = "anna.step.getResults.repeatMasker.fileName";
	private LogDispatcher logger;
	
	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		super.init(context);
		this.logger = new LogDispatcherImpl(context);
	}
	
	public boolean canBeSkipped(DataProxy data)
			throws StepExecutionException {
		logger.debug(this, "cannot be skipped");
		return false;
	}

	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			final ArrayList<? extends GFFElement> elements = data
					.getRepeatMaskerGff();
			// TODO predicted genes may be size==0
			logger.debug(this, "requirements satisfied="+(elements != null && elements.size() != 0));
			return (elements != null && elements.size() != 0);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
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
				System.out.println(this + ": writing repeatmasker gff to "
						+ outFile);
				final GFFFile file = new GFFFile(data
						.getRepeatMaskerGff());
				file.write(outFile);
			}
		} catch (Throwable t){t.printStackTrace(); System.exit(1);}
		return success;
	}

	private boolean checkOutDir(File outFile) {
		if (!outFile.exists()) {
			logger.info(this, ": " + outFile
					+ " does not exist, creating");
			final boolean b = outFile.mkdirs();
			return b;
		}
		return outFile.canWrite();
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName(); 
	}

}