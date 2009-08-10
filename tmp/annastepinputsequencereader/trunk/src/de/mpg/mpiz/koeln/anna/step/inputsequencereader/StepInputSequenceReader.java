package de.mpg.mpiz.koeln.anna.step.inputsequencereader;

import java.io.File;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAFileImpl;
import de.bioutils.fasta.FASTASequence;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public class StepInputSequenceReader extends AbstractStep {

	private final static String INFILE_KEY = "anna.step.inputsequencereader.infile";
	private LogDispatcher logger;

	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		try{
		super.init(context);
		logger = new LogDispatcherImpl(context);
		}catch(Throwable t){
			StepUtils.handleException(this, t, logger);
		}
	}

	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		logger.debug(this, "no requirements needed");
		return true;
	}

	public boolean canBeSkipped(DataProxy data)
			throws StepExecutionException {
		try {
			final boolean inputSequences = (data
					.getInputSequences() != null);
			final boolean inputSequencesSize = (data
					.getInputSequences().size() != 0);
			logger.debug(this, "need to run:");
			logger.debug(this, "\tinputSequences=" + inputSequences);
			logger.debug(this, "\tinputSequencesSize=" + inputSequencesSize);
			return (inputSequences && inputSequencesSize);
		}catch(Throwable t){
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		try {
			final File inFile = new File(getStepProperties().getProperty(
					INFILE_KEY));
			logger.debug(this, "reading file " + inFile);
			final ArrayList<? extends FASTASequence> fastas = new FASTAFileImpl(
					inFile, null).getElements();
			if (fastas == null || fastas.size() == 0) {
				logger.warn(this, "file " + inFile + " is invalid");
				return false;
			}
			logger.debug(this, "got input sequences:"
					+ fastas.iterator().next().getHeader() + " [...]");
			data.setInputSequences(fastas);
			return true;
		}catch(Throwable t){
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
