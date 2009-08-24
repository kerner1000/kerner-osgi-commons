package de.fh.giessen.ringversuch.controller;

import hssf.utils.WrongFileTypeException;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.model.ModelImpl;
import de.fh.giessen.ringversuch.model.settings.ModelSettings;
import de.fh.giessen.ringversuch.view.View;
import de.fh.giessen.ringversuch.view.ViewImpl;
import de.fh.giessen.ringversuch.view.settings.ViewSettings;
import de.fh.giessen.ringversuch.view.settings.ViewSettingsImpl;
import de.kerner.commons.StringUtils;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-08-24
 * @Strings all good
 * 
 */
class ControllerImpl implements Controller {

	private final static Logger LOGGER = Logger.getLogger(ControllerImpl.class);
	// private final ExecutorService out = Executors.newCachedThreadPool();
	private final ExecutorService in = Executors.newCachedThreadPool();

	private volatile View view;
	private volatile Model model;

	private static void debug(Throwable t, Object... message) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(StringUtils.getString(message), t);
	}

	private static void debug(Object... message) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(StringUtils.getString(message));
	}

	private static void info(Object... message) {
		if (LOGGER.isInfoEnabled())
			LOGGER.info(StringUtils.getString(message));
	}

	@Override
	public void setView(View view) {
		debug("new view=", view);
		this.view = view;
	}

	@Override
	public void setModel(Model model) {
		debug("new model=", model);
		this.model = model;
	}

	@Override
	public boolean setSelectedFiles(final File[] inputFiles) {
		if (model == null) {
			final String m = "model not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		try {
			model.setSelectedFiles(inputFiles);
			info(inputFiles.length, " ",
					Preferences.Controller.FILES_LOADED_GOOD);
			detect();
		} catch (WrongFileTypeException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.printMessage(e.getLocalizedMessage(), true);
			view.showError(e.getLocalizedMessage());
			info(inputFiles.length, " ",
					Preferences.Controller.FILES_LOADED_BAD);
			return false;
		}
		return true;
	}

	@Override
	public void detect() {
		LOGGER.info("detecting settings");
		// if we do not run this in extra thread, app freezes.
		try {
			in.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					detectProbeCell();
					detectLaborCell();
					detectColumnOfSubstances();
					detectValuesBeginCell();
					detectValuesEndCell();
					final ModelSettings ms = model.getSettings();
					view.setSettings(SettingsConverter
							.modelSettingsToViewSettings(ms));
					return null;
				}
			});
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			String m = StringUtils.getString(Preferences.Controller.DETECT_BAD,
					" (", e.getLocalizedMessage(), ")");
			view.printMessage(m, true);
		}
	}

	private void detectColumnOfSubstances() throws Exception {
		debug("auto-detecting settings column of substances");

		// no need to run this in an extra thread, because model method is
		// already implemented to work in a separate thread
		model.detectColumnOfSubstances();

		debug("auto-detecting settings column of substances successful");
	}

	private void detectValuesEndCell() throws Exception {
		debug("auto-detecting settings values end cell");
		model.detectValuesEndCell();
		debug("auto-detecting settings values end cell successful");
	}

	private void detectValuesBeginCell() throws Exception {
		debug("auto-detecting settings values begin cell");
		model.detectValuesBeginCell();
		debug("auto-detecting settings values begin cell successful");
	}

	private void detectLaborCell() throws Exception {
		debug("auto-detecting settings labor cell");
		model.detectLaborCell();
		debug("auto-detecting settings labor cell successful");
	}

	private void detectProbeCell() throws Exception {
		debug("auto-detecting settings probe cell");
		model.detectProbeCell();
		debug("auto-detecting settings probe cell successful");
	}

	@Override
	public void start() {
		// first check validity of settings.
		// settings may have bypassed any validation if they have been auto
		// detected.
		try {
			// if we do not run this in extra thread, app freezes.
			in.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					view.setWorking();
					model.checkSettings();
					model.start();
					return null;
				}
			}).get();
			// view.setReady();
		} catch (CancellationException e) {
			// e.printStackTrace();
			debug(e, e.getLocalizedMessage());
			info(Preferences.Controller.CANCELED);
			view.printMessage(Preferences.Controller.CANCELED, false);
		} catch (Exception e) {
			final String m = StringUtils.getString(
					Preferences.Controller.FAILED, " (", e
							.getLocalizedMessage(), ")");
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError(m);
			view.printMessage(m, true);
		} finally {
			// setting view to "online" instead of "ready"
			// because of
			// errors is maybe a good idea
			view.setReady();
		}
	}

	@Override
	public void cancel() {
		debug("cancelling");
		model.cancel();
		view.setReady();
	}

	@Override
	public void done(boolean b) {
		// TODO boolean b ??
		debug("done. setting view ready.");
		view.setReady();
	}

	@Override
	public void setProgress(final int current, final int max) {
		debug("setting progress to ", current, "/", max);
		view.setProgress(current, max);
	}

	@Override
	public boolean loadSettings(final File file) {
		try {
			// runs in own thread because of static SettingsConverter method.
			return in.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					LOGGER.debug("loading settings");
					model.setSettings(SettingsConverter
							.propertiesToModelSettings(SettingsConverter
									.fileToProperties(file)));
					final String m = StringUtils.getString(
							Preferences.Controller.SETTINGS_LOADED_GOOD,
							" from ", file);
					info(m);
					view.printMessage(m, false);
					return Boolean.TRUE;
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError(StringUtils.getString(
					Preferences.Controller.SETTINGS_LOADED_BAD, " (", e
							.getLocalizedMessage(), ")"));
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean saveSettings(final ViewSettings settings) {
		try {
			// runs in own thread because of static SettingsConverter method.
			return in.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					LOGGER.debug("saving settings");
					model.setSettings(SettingsConverter
							.viewSettingsToModelSettings(settings));
					SettingsConverter.propertiesToFile(SettingsConverter
							.settingsToProperties(model.getSettings()),
							new File(Preferences.SETTINGS_FILE));
					info(Preferences.Controller.SETTINGS_SAVED_GOOD);
					view.printMessage(
							Preferences.Controller.SETTINGS_SAVED_GOOD, false);
					return Boolean.TRUE;
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(
					Preferences.Controller.SETTINGS_SAVED_BAD, " (", e
							.getLocalizedMessage(), ")");
			view.showError(m);
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean setSettings(final ViewSettings settings) {
		try {
			debug("setting settings");
			model.setSettings(SettingsConverter
					.viewSettingsToModelSettings(settings));
			info(Preferences.Controller.SETTINGS_SET_GOOD);
			view.printMessage(Preferences.Controller.SETTINGS_SET_GOOD, false);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(
					Preferences.Controller.SETTINGS_SET_BAD, " (", e
							.getLocalizedMessage(), ")");
			view.showError(m);
			return false;
		}
	}

	@Override
	public void printMessage(final String message,
			final boolean isError) {
		if (view == null) {
			final String m = "view not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		debug("printMessage=" + message);
		view.printMessage(message, isError);
	}

	@Override
	public void setOutDir(final File selectedDir) {
		if (model == null) {
			final String m = "model not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		debug("setOutDir=" + selectedDir);
		model.setOutDir(selectedDir);
	}

	@Override
	public void showError(final String message) {
		if (view == null) {
			final String m = "view not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		debug("showError=" + message);
		view.showError(message);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure(Preferences.LOG_PROPERTIES);
		debug("starting up");
		final String m = StringUtils.getString("System properties:",
				Preferences.NEW_LINE, "\t", System.getProperties());
		debug(m);
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
			view.setSettings(SettingsConverter
					.modelSettingsToViewSettings(model.getSettings()));
			final String m2 = StringUtils.getString(
					Preferences.Controller.SETTINGS_LOADED_GOOD, " (from ", f,
					")");
			info(m2);
			controller.printMessage(m2, false);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			controller.printMessage(e.getLocalizedMessage(), false);
			controller.printMessage("loading default settings", false);
			view.setSettings(new ViewSettingsImpl());
		}
		view.setOnline();
		debug("view created and online");
	}
}
