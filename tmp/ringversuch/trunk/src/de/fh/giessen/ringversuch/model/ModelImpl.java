package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.controller.Controller;

public class ModelImpl implements Model {

	private final ExecutorService exe = Executors.newSingleThreadExecutor();
	private final static Logger LOGGER = Logger.getLogger(ModelImpl.class);
	private final Controller controller;
	private File outDir;
	private File[] inputFiles;
	private SettingsModel settings;
	private Future<Boolean> currentJob;

	public ModelImpl(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void setOutDir(File selectedDir) {
		this.outDir = selectedDir;
		LOGGER.info("files will be written to " + outDir);
		controller.printMessage("files will be written to " + outDir, false);
	}

	@Override
	public void setSelectedFiles(File[] inputFiles) {
		this.inputFiles = inputFiles;
		LOGGER.info("input files: " + Arrays.asList(inputFiles));
		controller.printMessage("selected " + Arrays.asList(inputFiles).size()
				+ " file(s)", false);
	}

	@Override
	public void start() {
		LOGGER.info("starting...");
		currentJob = exe.submit(new Worker(inputFiles, outDir, settings,
				new WorkMonitor(controller)));
		// TODO move exception handling to controller
		try {
			final boolean success = currentJob.get();
			if (!success) {
				LOGGER.fatal("work was not successful!");
				controller.printMessage("failed!", true);
				return;
			}
		} catch (CancellationException e) {
			LOGGER.info("work cancelled", e);
			controller.printMessage("work cancelled", false);
			return;
		} catch (Exception e) {
			LOGGER.fatal("work was not successful!", e);
			controller.printMessage(
					"failed! (" + e.getLocalizedMessage() + ")", true);
			return;
		} finally {
			LOGGER.debug("done, restoring");
			controller.done(false);
		}
		LOGGER.info("...done!");
		controller.printMessage("done!", false);
	}

	@Override
	public SettingsModel getSettings() {
		return settings;
	}

	@Override
	public void setSettings(SettingsModel settings) {
		LOGGER.debug("new settings: " + settings);
		this.settings = settings;
	}

	@Override
	public synchronized void cancel() {
		currentJob.cancel(true);
	}
}
