package de.fh.giessen.ringversuch.controller;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.model.InvalidSettingsException;
import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.model.ModelImpl;
import de.fh.giessen.ringversuch.view.SettingsView;
import de.fh.giessen.ringversuch.view.SettingsViewImpl;
import de.fh.giessen.ringversuch.view.View;
import de.fh.giessen.ringversuch.view.ViewImpl;

public class ControllerImpl implements Controller {

	private final static Logger LOGGER = Logger.getLogger(ControllerImpl.class);
	private final ExecutorService exe = Executors.newSingleThreadExecutor();
	private View view;
	private Model model;

	@Override
	public synchronized void printMessage(String message, boolean isError) {
		LOGGER.debug("printMessage=" + message);
		if (view != null)
			view.printMessage(message, isError);
	}

	@Override
	public synchronized void setOutDir(File selectedDir) {
		LOGGER.debug("setOutDir=" + selectedDir);
		if (model != null)
			model.setOutDir(selectedDir);
		else
			LOGGER.fatal("model not initialized jet",
					new NullPointerException("model not initialized jet"));
	}

	@Override
	public synchronized void showError(String message) {
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
		try {
			final File f = new File(Preferences.SETTINGS_FILE);
			model.setSettings(SettingsConverter
					.propertiesToModelSettings(SettingsConverter
							.fileToProperties(f)));
			view.setSettings(SettingsConverter.modelSettingsToViewSettings(model.getSettings()));
			final String message = "successfully loaded settings from " + f;
			LOGGER.info(message);
			controller.printMessage(message,
					false);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			controller.printMessage(e.getLocalizedMessage(), false);
			controller.printMessage("loading default settings", false);
			view.setSettings(new SettingsViewImpl());
		}

		view.setOnline();
		LOGGER.debug("view created and online");
	}

	@Override
	public synchronized void setView(View view) {
		LOGGER.debug("setView=" + view);
		this.view = view;
	}

	@Override
	public synchronized void setModel(Model model) {
		LOGGER.debug("setModel=" + model);
		this.model = model;
	}

	@Override
	public synchronized void setSelectedFiles(File[] inputFiles) {
		LOGGER.info("setSelectedFiles=" + inputFiles);
		if (model != null)
			model.setSelectedFiles(inputFiles);
		else
			LOGGER.fatal("model not initialized jet",
					new NullPointerException("model not initialized jet"));
	}

	@Override
	public synchronized void start() {
		exe.submit(new Runnable() {
			@Override
			public void run() {
				view.setWorking();
				model.start();
//				view.setOnline();
			}
		});
	}
	
	@Override
	public synchronized void done(boolean b) {
		LOGGER.debug("done. setting view ready.");
		view.setReady();
	}
	
	@Override
	public synchronized void setProgress(int current, int max) {
		view.setProgress(current, max);
	}

	@Override
	public synchronized boolean loadSettings(File file) {
		try {
			LOGGER.debug("loading settings");
			model.setSettings(SettingsConverter.propertiesToModelSettings(SettingsConverter.fileToProperties(file)));
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError("could not load settings: " + e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public synchronized boolean saveSettings(SettingsView settings) {
		 try {
			 LOGGER.debug("saving settings");
		 model.setSettings(SettingsConverter.viewSettingsToModelSettings(settings));
			SettingsConverter.propertiesToFile(SettingsConverter.settingsToProperties(model.getSettings()), new File(Preferences.SETTINGS_FILE));
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError("could not save settings: " + e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public synchronized boolean setSettings(SettingsView settings) {
		 try {
			 LOGGER.debug("setting settings");
			model.setSettings(SettingsConverter.viewSettingsToModelSettings(settings));
			LOGGER.debug("settings successfull set");
			return true;
		} catch (InvalidSettingsException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError("could not set settings: " + e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public synchronized void cancel() {
		model.cancel();
	}

}
