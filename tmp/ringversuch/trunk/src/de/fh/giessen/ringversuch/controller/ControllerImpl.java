package de.fh.giessen.ringversuch.controller;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.model.ModelImpl;
import de.fh.giessen.ringversuch.view.View;
import de.fh.giessen.ringversuch.view.ViewImpl;

public class ControllerImpl implements Controller {

	private final static Logger LOGGER = Logger.getLogger(ControllerImpl.class);
	private View view;
	private Model model;

	@Override
	public void printMessage(String message, boolean isError) {
		LOGGER.debug("printMessage=" + message);
		if (view != null)
			view.printMessage(message, isError);
	}

	@Override
	public void setOutDir(File selectedDir) {
		LOGGER.debug("setOutDir=" + selectedDir);
		if (model != null)
			model.setOutDir(selectedDir);
		else
			LOGGER.fatal("model not initialized jet", new NullPointerException());
	}

	@Override
	public void showError(String message) {
		LOGGER.debug("showError=" + message);
		if (view != null)
			view.showError(message);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure(Preferences.LOG_PROPERTIES);
		LOGGER.debug("starting up");
		final Controller controller = new ControllerImpl();
		final Model model = new ModelImpl(controller);
		final View view = new ViewImpl(controller);
		controller.setModel(model);
		controller.setView(view);
		
		// must be done here, because model is not set after init of view
		try{
			final File f = new File(Preferences.SETTINGS_FILE);
			model.loadSettings(f);
			view.setSettingsOut(model.getCurrentSettings());
			controller.printMessage("successfully loaded settings from " + f, false);
		}catch(Exception e){
			LOGGER.error(e.getLocalizedMessage(), e);
			controller.printMessage(e.getLocalizedMessage(), false);
			controller.printMessage("loading default settings", false);
			view.setSettingsOut(model.getDefaultSettings());
		}
		
		view.setOnline();
		LOGGER.debug("view created and online");
	}

	@Override
	public void setView(View view) {
		LOGGER.debug("setView=" + view);
		this.view = view;
	}

	@Override
	public void setModel(Model model) {
		LOGGER.debug("setModel=" + model);
		this.model = model;
	}

	@Override
	public void setSelectedFiles(File[] inputFiles) {
		LOGGER.info("setSelectedFiles=" + inputFiles);
		if (model != null)
			model.setSelectedFiles(inputFiles);
		else
			LOGGER.fatal("model not initialized jet", new NullPointerException());
	}

	@Override
	public void start() {
		view.setWorking();
		model.start();
		view.setOnline();
	}

	@Override
	public Properties getDefaultSettings() {
		if (model != null)
			return model.getDefaultSettings();
		else
			LOGGER.fatal("model not initialized jet", new NullPointerException());
		return null;
	}

	@Override
	public boolean loadSettings(File file) {
		if (model != null)
			return model.loadSettings(file);
		else
			LOGGER.fatal("model not initialized jet", new NullPointerException());
		return false;

	}

	@Override
	public boolean saveSettings()
			{
		if (model != null)
			return model.saveSettings();
		else
			LOGGER.fatal("model not initialized jet", new NullPointerException());
		return false;
	}

	@Override
	public Properties getSettings() {
		if (model != null)
			return model.getCurrentSettings();
		else
			LOGGER.fatal("model not initialized jet", new NullPointerException());
		return null;
	}

	@Override
	public boolean setSettingsIn(Properties settings) {
		if (model != null)
			return model.setSettings(settings);
		else
			LOGGER.fatal("model not initialized jet", new NullPointerException());
		return false;
	}

	@Override
	public void setSettingsOut(Properties settings) {
		view.setSettingsOut(settings);
	}
	
	
}
