package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.controller.ControllerOut;
import de.fh.giessen.ringversuch.model.settings.ModelSettings;

/**
 * @ThreadSave
 * @author Alexander Kerner
 *
 */
public class ModelImpl implements Model {

	private final ExecutorService modelThread = Executors.newSingleThreadExecutor();
	private final static Logger LOGGER = Logger.getLogger(ModelImpl.class);
	private final ControllerOut controller;
	private File outDir;
	private File[] inputFiles;
	private ModelSettings settings;
	private Future<Boolean> currentJob;

	public ModelImpl(ControllerOut controller) {
		this.controller = controller;
	}

	@Override
	public synchronized void setOutDir(File selectedDir) {
		this.outDir = selectedDir;
		LOGGER.info("files will be written to " + outDir);
		controller.printMessage("files will be written to " + outDir, false);
	}

	@Override
	public synchronized void setSelectedFiles(File[] inputFiles) {
		this.inputFiles = inputFiles;
		LOGGER.info("input files: " + Arrays.asList(inputFiles));
		controller.printMessage("selected " + Arrays.asList(inputFiles).size()
				+ " file(s)", false);
	}

	@Override
	public synchronized boolean start() throws CancellationException, InterruptedException, ExecutionException {
		LOGGER.info("starting...");
			currentJob = modelThread.submit(new Worker(inputFiles, outDir, settings,
					new WorkMonitor(controller)));
				final Boolean success = currentJob.get();
				return success;
		
	}

	@Override
	public synchronized ModelSettings getSettings() {
		return settings;
	}

	@Override
	public synchronized void setSettings(ModelSettings settings) {
		LOGGER.debug("new settings: " + settings);
		this.settings = settings;
	}

	/**
	 * Must not be synchronized !!
	 */
	@Override
	public void cancel() {
		currentJob.cancel(true);
	}
}
