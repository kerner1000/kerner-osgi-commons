package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.fh.giessen.ringversuch.controller.Controller;
import de.fh.giessen.ringversuch.controller.ControllerImpl;

public class ModelImpl implements Model {

	private final static Logger LOGGER = Logger.getLogger(ModelImpl.class);
	private final Controller controller;
	private final SettingsManager settings = SettingsManagerImpl.INSTANCE;
	private File outDir;
	private File[] inputFiles;
	private final ExecutorService exe = Executors.newSingleThreadExecutor();
	
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
		controller.printMessage("selected " + Arrays.asList(inputFiles).size() + " file(s)", false);
	}

	@Override
	public void start() {
		LOGGER.info("starting...");
		Future<Boolean> f = exe.submit(new Worker(inputFiles, outDir));
		try {
			final boolean success = f.get();
			if(!success){
				LOGGER.fatal("work was not successful!");
				controller.printMessage("failed!", true);
				return;
			}
		} catch (Exception e){
			LOGGER.fatal("work was not successful!", e);
			return;
		}
		LOGGER.info("...done!");
		controller.printMessage("done!", false);
	}

	@Override
	public void loadSettings(File settingsFile) throws IOException, InvalidSettingsException {
		LOGGER.debug("loading settings from " + settingsFile);
		settings.loadSettings(settingsFile);	
	}
	
}
