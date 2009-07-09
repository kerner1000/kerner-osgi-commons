package de.mpg.mpiz.koeln.kerner.anna.step.inputsequencereader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessObserver;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;

public class StepInputSequenceReader extends AbstractStep {

	private final static String INFILE_KEY = "anna.step.inputsequencereader.infile";
	private LogDispatcher logger;

	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		super.init(context);
		logger = new LogDispatcherImpl(context);
	}

	@Override
	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		logger.debug(this, "no requirements needed");
		return true;
	}

	@Override
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
		} catch (DataBeanAccessException e) {
			logger.error(this, e.toString(), e);
			throw new StepExecutionException(e);
		}
	}

	@Override
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		try {
			final File inFile = new File(getStepProperties().getProperty(
					INFILE_KEY));
			logger.debug(this, "reading file " + inFile);
			final ArrayList<? extends FASTASequence> fastas = new FASTAFile(
					inFile).getSequences();
			if (fastas == null || fastas.size() == 0) {
				logger.warn(this, "file " + inFile + " is invalid");
				return false;
			}
			logger.debug(this, "got input sequences:"
					+ fastas.iterator().next().getHeader() + " [...]");
			data.setInputSequences(fastas);
			return true;
		} catch (IOException e) {
			logger.error(this, e.toString(), e);
			return false;
		} catch (DataBeanAccessException e) {
			logger.error(this, e.toString(), e);
			return false;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
