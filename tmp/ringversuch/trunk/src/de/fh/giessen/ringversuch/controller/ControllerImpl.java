package de.fh.giessen.ringversuch.controller;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.model.ModelImpl;
import de.fh.giessen.ringversuch.view.View;
import de.fh.giessen.ringversuch.view.ViewImpl;

public class ControllerImpl implements Controller {

	private final static String LOG_PROPERTIES = "/home/alex/Dropbox/log.properties";
	private final static Logger LOGGER = Logger.getLogger(ControllerImpl.class);
	private View view;
	private Model model;
	
	@Override
	public void printMessage(String message, boolean isError) {
		view.printMessage(message, isError);
	}

	@Override
	public void setOutDir(File selectedDir) {
		model.setOutDir(selectedDir);
	}

	@Override
	public void showError(String message) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args){
		PropertyConfigurator.configure(LOG_PROPERTIES);
		LOGGER.debug("starting up");
		final Controller controller = new ControllerImpl();
		final View view = new ViewImpl(controller);
		final Model model = new ModelImpl(controller);
		controller.setView(view);
		controller.setModel(model);
		view.setOnline();
		LOGGER.debug("view created and online");
	}

	@Override
	public void setView(View view) {
		this.view = view;
	}

	@Override
	public void setModel(Model model) {
		this.model = model;
		// init default settings
		final File file = new File(new File(System.getProperty("user.dir")),
		"settings.ini");
		LOGGER.info("loading settings from " + file);
		
		// view may not be initialized yet
		if(view != null){
			view.printMessage("loading settings from " + file, false);
		}
		try {
			model.loadSettings(file);
		} catch (Exception e) {
			LOGGER.warn("could not load settings from " + file, e);
		}
	}

	@Override
	public void setSelectedFiles(File[] inputFiles) {
		model.setSelectedFiles(inputFiles);
	}

	@Override
	public void start() {
		view.setWorking();
		model.start();
		view.setOnline();
	}
}
