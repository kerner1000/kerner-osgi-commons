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
import de.fh.giessen.ringversuch.view.MainView;
import de.fh.giessen.ringversuch.view.SettingsView;
import de.fh.giessen.ringversuch.view.SwingView;
import de.fh.giessen.ringversuch.view.SwingViewManager;
import de.fh.giessen.ringversuch.view.SwingViewManagerImpl;
import de.fh.giessen.ringversuch.view.ViewOut;
import de.fh.giessen.ringversuch.view.ViewType;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettingsImpl;
import de.kerner.commons.StringUtils;

/**
 * 
 * @ThreadSave members are volatile. No atomic operations, that affect both
 *             members at one time.
 * @author Alexander Kerner
 * @lastVisit 2009-08-25
 * @Strings all good
 * 
 */
class ControllerImpl implements Controller {

	private final static Logger LOGGER = Logger.getLogger(ControllerImpl.class);

	// TODO: this is only of type swingview instead of viewOut because of some
	// casting problems...
	private volatile SwingView view;
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
		this.view = viewIn;
	}

	@Override
	public void setModel(Model model) {
		debug("new model=", model);
		this.model = model;
	}

	@Override
	public boolean incomingSetSelectedFiles(final File[] inputFiles) {
		if (model == null) {
			final String m = "model not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		try {
			model.setSelectedFiles(inputFiles);
			info(inputFiles.length, " ",
					Preferences.Controller.FILES_LOADED_GOOD);
			incomingDetect();
		} catch (WrongFileTypeException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.outgoingPrintMessage(e.getLocalizedMessage(), true);
			view.outgoingShowError(e.getLocalizedMessage());
			info(inputFiles.length, " ",
					Preferences.Controller.FILES_LOADED_BAD);
			return false;
		}
		return true;
	}

	@Override
	public void incomingDetect() {
		LOGGER.info("detecting settings");
		try {
			detectProbeCell();
			detectLaborCell();
			detectColumnOfSubstances();
			detectValuesBeginCell();
			detectValuesEndCell();
			final ModelSettings ms = model.getSettings();
			view.outgoingSetSettings(SettingsConverter
					.modelSettingsToViewSettings(ms));
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			String m = StringUtils.getString(Preferences.Controller.DETECT_BAD,
					" (", e.getLocalizedMessage(), ")");
			view.outgoingPrintMessage(m, true);
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
	public void incomingStart() {
		// first check validity of settings.
		// settings may have bypassed any validation if they have been auto
		// detected.
		try {
			view.outgoingSetWorking();
			model.checkSettings();
			model.start();
		} catch (CancellationException e) {
			// e.printStackTrace();
			debug(e, e.getLocalizedMessage());
			info(Preferences.Controller.CANCELED);
			view.outgoingPrintMessage(Preferences.Controller.CANCELED, false);
		} catch (Exception e) {
			final String m = StringUtils.getString(
					Preferences.Controller.FAILED, " (", e
							.getLocalizedMessage(), ")");
			LOGGER.error(e.getLocalizedMessage(), e);
			view.outgoingShowError(m);
			view.outgoingPrintMessage(m, true);
		} finally {
			// setting view to "online" instead of "ready"
			// because of
			// errors is maybe a good idea
			view.outgoingSetReady();
		}
	}

	@Override
	public void incomingCancel() {
		debug("cancelling");
		model.cancel();
		view.outgoingSetReady();
	}

	@Override
	public void outgoingDone(boolean b) {
		// TODO boolean b ??
		debug("done. setting view ready.");
		view.outgoingSetReady();
	}

	@Override
	public void outgoingSetProgress(final int current, final int max) {
		debug("setting progress to ", current, "/", max);
		view.outgoingSetProgress(current, max);
	}

	@Override
	public boolean incomingLoadSettings(final File file) {
		try {
			LOGGER.debug("loading settings");
			model.setSettings(SettingsConverter
					.propertiesToModelSettings(SettingsConverter
							.fileToProperties(file)));
			final String m = StringUtils
					.getString(Preferences.Controller.SETTINGS_LOADED_GOOD,
							" from ", file);
			info(m);
			view.outgoingPrintMessage(m, false);
			return Boolean.TRUE;

		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.outgoingShowError(StringUtils.getString(
					Preferences.Controller.SETTINGS_LOADED_BAD, " (", e
							.getLocalizedMessage(), ")"));
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean incomingSaveSettings(final ViewTypeSettings settings) {
		try {

			LOGGER.debug("saving settings");
			model.setSettings(SettingsConverter
					.viewSettingsToModelSettings(settings));
			SettingsConverter.propertiesToFile(SettingsConverter
					.settingsToProperties(model.getSettings()), new File(
					Preferences.SETTINGS_FILE));
			info(Preferences.Controller.SETTINGS_SAVED_GOOD);
			view
					.outgoingPrintMessage(Preferences.Controller.SETTINGS_SAVED_GOOD,
							false);
			return true;

		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(
					Preferences.Controller.SETTINGS_SAVED_BAD, " (", e
							.getLocalizedMessage(), ")");
			view.outgoingShowError(m);
			return false;
		}
	}

	@Override
	public boolean incomingSetSettings(final ViewTypeSettings settings) {
		try {
			debug("setting settings");
			model.setSettings(SettingsConverter
					.viewSettingsToModelSettings(settings));
			info(Preferences.Controller.SETTINGS_SET_GOOD);
			view.outgoingPrintMessage(Preferences.Controller.SETTINGS_SET_GOOD, false);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(
					Preferences.Controller.SETTINGS_SET_BAD, " (", e
							.getLocalizedMessage(), ")");
			view.outgoingShowError(m);
			return false;
		}
	}

	@Override
	public void outgoingPrintMessage(final String message, final boolean isError) {
		if (view == null) {
			final String m = "view not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		debug("printMessage=" + message);
		view.outgoingPrintMessage(message, isError);
	}

	@Override
	public void incomingSetOutDir(final File selectedDir) {
		if (model == null) {
			final String m = "model not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		debug("setOutDir=" + selectedDir);
		model.setOutDir(selectedDir);
	}

	@Override
	public void outgoingShowError(final String message) {
		if (view == null) {
			final String m = "view not initialized jet";
			LOGGER.fatal(m);
			throw new RuntimeException(m);
		}
		debug("showError=" + message);
		view.outgoingShowError(message);
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
				final SwingViewManager manager = new SwingViewManagerImpl(
						controller);
				try {
					manager.addView(ViewType.MAIN, new MainView(manager));
					manager.addView(ViewType.SETTINGS,
							new SettingsView(manager));
					controller.setViewIn(manager);

					// must be done here, because model is not set after init of
					// view

					final File f = new File(Preferences.SETTINGS_FILE);
					model.setSettings(SettingsConverter
							.propertiesToModelSettings(SettingsConverter
									.fileToProperties(f)));
					manager.outgoingSetSettings(SettingsConverter
							.modelSettingsToViewSettings(model.getSettings()));
					final String m2 = StringUtils.getString(
							Preferences.Controller.SETTINGS_LOADED_GOOD,
							" (from ", f, ")");
					info(m2);
					controller.outgoingPrintMessage(m2, false);
				} catch (Exception e) {
					LOGGER.error(e.getLocalizedMessage(), e);
					controller.outgoingPrintMessage(e.getLocalizedMessage(),
							false);
					controller.outgoingPrintMessage("loading default settings",
							false);
					manager.outgoingSetSettings(new ViewTypeSettingsImpl());
				}
				manager.showView();
				manager.outgoingSetOnline();
				debug("view created and online");
			}
		});
	}

	@Override
	public void incomingShutdown() {
		LOGGER.info("shutting down Application");
		model.shutdown();
		LOGGER.info("Application dead");
		view.outgoingShutdown();
	}

	@Override
	public void outgoingShutdown() {
		// Model will never shutdown the app.
		// überflüssgige methode ??
	}
}
