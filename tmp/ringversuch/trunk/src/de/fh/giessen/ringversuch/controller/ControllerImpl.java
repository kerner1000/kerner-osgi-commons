package de.fh.giessen.ringversuch.controller;

import hssf.utils.WrongFileTypeException;

import java.io.File;
import java.util.concurrent.CancellationException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.model.ModelImpl;
import de.fh.giessen.ringversuch.model.settings.ModelSettings;
import de.fh.giessen.ringversuch.view.ViewImpl;
import de.fh.giessen.ringversuch.view.ViewIn;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettingsImpl;
import de.fh.giessen.ringversuch.view2.MainView;
import de.fh.giessen.ringversuch.view2.SettingsView;
import de.fh.giessen.ringversuch.view2.SwingView;
import de.fh.giessen.ringversuch.view2.SwingViewManager;
import de.fh.giessen.ringversuch.view2.SwingViewManagerImpl;
import de.fh.giessen.ringversuch.view2.ViewType;
import de.kerner.commons.StringUtils;

/**
 * 
 * @ThreadSave members are volatile. No atomic operations, that affect both members at one time.
 * @author Alexander Kerner
 * @lastVisit 2009-08-25
 * @Strings all good
 * 
 */
class ControllerImpl implements Controller {

	private final static Logger LOGGER = Logger.getLogger(ControllerImpl.class);
	private volatile SwingView viewIn;
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
	public void setViewIn(SwingView viewIn) {
		debug("new in view=", viewIn);
		this.viewIn = viewIn;
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
			viewIn.printMessage(e.getLocalizedMessage(), true);
			viewIn.showError(e.getLocalizedMessage());
			info(inputFiles.length, " ",
					Preferences.Controller.FILES_LOADED_BAD);
			return false;
		}
		return true;
	}

	@Override
	public void detect() {
		LOGGER.info("detecting settings");
		try {
			detectProbeCell();
			detectLaborCell();
			detectColumnOfSubstances();
			detectValuesBeginCell();
			detectValuesEndCell();
			final ModelSettings ms = model.getSettings();
			viewIn.setSettings_view(SettingsConverter.modelSettingsToViewSettings(ms));
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			String m = StringUtils.getString(Preferences.Controller.DETECT_BAD,
					" (", e.getLocalizedMessage(), ")");
			viewIn.printMessage(m, true);
		}
	}

	private void detectColumnOfSubstances() throws Exception {
		debug("auto-detecting settings column of substances");
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
			viewIn.setWorking();
			model.checkSettings();
			model.start();
		} catch (CancellationException e) {
			// e.printStackTrace();
			debug(e, e.getLocalizedMessage());
			info(Preferences.Controller.CANCELED);
			viewIn.printMessage(Preferences.Controller.CANCELED, false);
		} catch (Exception e) {
			final String m = StringUtils.getString(
					Preferences.Controller.FAILED, " (", e
							.getLocalizedMessage(), ")");
			LOGGER.error(e.getLocalizedMessage(), e);
			viewIn.showError(m);
			viewIn.printMessage(m, true);
		} finally {
			// setting view to "online" instead of "ready"
			// because of
			// errors is maybe a good idea
			viewIn.setReady();
		}
	}

	@Override
	public void cancel() {
		debug("cancelling");
		model.cancel();
		viewIn.setReady();
	}

	@Override
	public void done(boolean b) {
		// TODO boolean b ??
		debug("done. setting view ready.");
		viewIn.setReady();
	}

	@Override
	public void setProgress(final int current, final int max) {
		debug("setting progress to ", current, "/", max);
		viewIn.setProgress(current, max);
	}

	@Override
	public boolean loadSettings(final File file) {
		try {
			
					LOGGER.debug("loading settings");
					model.setSettings(SettingsConverter
							.propertiesToModelSettings(SettingsConverter
									.fileToProperties(file)));
					final String m = StringUtils.getString(
							Preferences.Controller.SETTINGS_LOADED_GOOD,
							" from ", file);
					info(m);
					viewIn.printMessage(m, false);
					return Boolean.TRUE;
				
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			viewIn.showError(StringUtils.getString(
					Preferences.Controller.SETTINGS_LOADED_BAD, " (", e
							.getLocalizedMessage(), ")"));
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean saveSettings(final ViewTypeSettings settings) {
		try {
			
					LOGGER.debug("saving settings");
					model.setSettings(SettingsConverter
							.viewSettingsToModelSettings(settings));
					SettingsConverter.propertiesToFile(SettingsConverter
							.settingsToProperties(model.getSettings()),
							new File(Preferences.SETTINGS_FILE));
					info(Preferences.Controller.SETTINGS_SAVED_GOOD);
					viewIn.printMessage(
							Preferences.Controller.SETTINGS_SAVED_GOOD, false);
					return true;
				
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(
					Preferences.Controller.SETTINGS_SAVED_BAD, " (", e
							.getLocalizedMessage(), ")");
			viewIn.showError(m);
			return false;
		}
	}

	@Override
	public boolean setSettings_controller(final ViewTypeSettings settings) {
		try {
			debug("setting settings");
			model.setSettings(SettingsConverter
					.viewSettingsToModelSettings(settings));
			info(Preferences.Controller.SETTINGS_SET_GOOD);
			viewIn.printMessage(Preferences.Controller.SETTINGS_SET_GOOD, false);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(
					Preferences.Controller.SETTINGS_SET_BAD, " (", e
							.getLocalizedMessage(), ")");
			viewIn.showError(m);
			return false;
		}
	}

	@Override
	public void printMessage(final String message, final boolean isError) {
		if (viewIn == null) {
			final String m = "view not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		debug("printMessage=" + message);
		viewIn.printMessage(message, isError);
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
		if (viewIn == null) {
			final String m = "view not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		debug("showError=" + message);
		viewIn.showError(message);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure(Preferences.LOG_PROPERTIES);
		debug("starting up");
		final String m = StringUtils.getString("System properties:",
				Preferences.NEW_LINE, "\t", System.getProperties());
		debug(m);
	
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final Controller controller = new ControllerImpl();
				final Model model = new ModelImpl(controller);
				controller.setModel(model);
				final SwingViewManager manager = new SwingViewManagerImpl();
				try {
					manager.addView(ViewType.MAIN, new MainView(manager, controller));
					manager.addView(ViewType.SETTINGS, new SettingsView(manager, controller));
					controller.setViewIn(manager);

					// must be done here, because model is not set after init of view
					
						final File f = new File(Preferences.SETTINGS_FILE);
						model.setSettings(SettingsConverter
								.propertiesToModelSettings(SettingsConverter
										.fileToProperties(f)));
						manager.setSettings_view(SettingsConverter
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
						manager.setSettings_view(new ViewTypeSettingsImpl());
					}
					manager.showView();
					manager.setOnline();
					debug("view created and online");
			}
		});
	}
}
