package de.fh.giessen.ringversuch.controller;

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
 * @lastVisit 2009-08-14
 * @Strings all good
 * 
 */
class ControllerImpl implements Controller {

	private final static Logger LOGGER = Logger.getLogger(ControllerImpl.class);
	private final ExecutorService out = Executors.newCachedThreadPool();
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
	public void setSelectedFiles(final File[] inputFiles) {
		in.submit(new Runnable() {
			@Override
			public void run() {
				if (model != null)
					model.setSelectedFiles(inputFiles);
				else {
					final String m = "model not initialized jet";
					LOGGER.fatal(m, new NullPointerException(m));
				}

				info(inputFiles.length, " ",
						Preferences.Controller.FILES_LOADED_GOOD);
			}
		});
		detect();
	}

	@Override
	public void start() {
		// first check validity of settings.
		// settings may have bypassed any validation if they have been auto
		// detected.
		try {
			if (in.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					debug("checking validity of settings");
					try {
						model.checkSettings();
						return Boolean.TRUE;
					} catch (Exception e) {
						final String m = StringUtils.getString("failed! (", e.getLocalizedMessage(), ")");
						LOGGER.error(e.getLocalizedMessage(), e);
						view.showError(m);
						view.printMessage(m, true);
						return Boolean.FALSE;
					}
				}
			}).get()) {
				in.submit(new Runnable() {
					@Override
					public void run() {
						try {
							view.setWorking();
							model.start();
						} catch (CancellationException e) {
							debug(e, e.getLocalizedMessage());
							info(Preferences.Controller.CANCELED);
							view.printMessage(Preferences.Controller.CANCELED, false);
						} catch (Exception e) {
							LOGGER.error(e.getLocalizedMessage(), e);
							final String m = StringUtils.getString(Preferences.Controller.FAILED, " (", e.getLocalizedMessage(), ")");
							view.showError(m);
							view.printMessage(m, true);
						} finally {
							// setting view to "online" instead of "ready"
							// because of
							// errors is maybe a good idea
							view.setReady();
						}
					}
				});
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(Preferences.Controller.FAILED, " (", e.getLocalizedMessage(), ")");
			view.showError(m);
			view.printMessage(m, true);
		}
	}

	@Override
	public void cancel() {
		in.submit(new Runnable() {
			@Override
			public void run() {
				debug("cancelling");
				model.cancel();
			}
		});
	}

	@Override
	public void done(boolean b) {
		// TODO boolean b ??
		out.submit(new Runnable() {
			@Override
			public void run() {
				debug("done. setting view ready.");
				view.setReady();
			}
		});
	}

	@Override
	public void setProgress(final int current, final int max) {
		out.submit(new Runnable() {
			@Override
			public void run() {
				debug("setting progress to ", current, "/", max);
				view.setProgress(current, max);
			}
		});
	}

	@Override
	public boolean loadSettings(final File file) {
		try {
			return in.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					LOGGER.debug("loading settings");
					model.setSettings(SettingsConverter
							.propertiesToModelSettings(SettingsConverter
									.fileToProperties(file)));
					final String m = StringUtils.getString(Preferences.Controller.SETTINGS_LOADED_GOOD, " from ", file);
					info(m);
					view.printMessage(m, false);
					return Boolean.TRUE;
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError(StringUtils.getString(
					Preferences.Controller.SETTINGS_LOADED_BAD, "(", e
							.getLocalizedMessage(), ")"));
			return Boolean.FALSE;
		}
	}

	@Override
	public synchronized void detect() {
		try {
			detectProbeCell();
			detectLaborCell();
			detectColumnOfSubstances();
			detectValuesBeginCell();
			detectValuesEndCell();
			final ModelSettings ms = model.getSettings();
			view.setSettings(SettingsConverter.modelSettingsToViewSettings(ms));
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			String m = StringUtils.getString(Preferences.Controller.DETECT_BAD, " (", e.getLocalizedMessage(), ")");
			view.printMessage(m, true);
		}
	}

	private void detectColumnOfSubstances() throws Exception {
		debug("auto-detecting settings column of substances");
		in.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				model.detectColumnOfSubstances();
				return null;
			}
		}).get();
		debug("auto-detecting settings column of substances successful");
	}

	private void detectValuesEndCell() throws Exception {
		debug("auto-detecting settings values end cell");
		in.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				model.detectValuesEndCell();
				return null;
			}
		}).get();
		debug("auto-detecting settings values end cell successful");
	}

	private void detectValuesBeginCell() throws Exception {
		debug("auto-detecting settings values begin cell");
		in.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				model.detectValuesBeginCell();
				return null;
			}
		}).get();
		debug("auto-detecting settings values begin cell successful");
	}

	private void detectLaborCell() throws Exception {
		debug("auto-detecting settings labor cell");
		in.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				model.detectLaborCell();
				return null;
			}
		}).get();
		debug("auto-detecting settings labor cell successful");
	}

	private void detectProbeCell() throws Exception {
		debug("auto-detecting settings probe cell");
		in.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				model.detectProbeCell();
				return null;
			}
		}).get();
		debug("auto-detecting settings probe cell successful");
	}

	@Override
	public boolean saveSettings(final ViewSettings settings) {
		try {
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
					view.printMessage(Preferences.Controller.SETTINGS_SAVED_GOOD, false);
					return Boolean.TRUE;
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(Preferences.Controller.SETTINGS_SAVED_BAD, " (", e.getLocalizedMessage(), ")");
			view.showError(m);
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean setSettings(final ViewSettings settings) {
		try {
			return in.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					debug("setting settings");
					model.setSettings(SettingsConverter
							.viewSettingsToModelSettings(settings));
					info(Preferences.Controller.SETTINGS_SET_GOOD);
					view.printMessage(Preferences.Controller.SETTINGS_SET_GOOD, false);
					return Boolean.TRUE;
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			final String m = StringUtils.getString(Preferences.Controller.SETTINGS_SET_BAD, " (" , e.getLocalizedMessage(), ")");
			view.showError(m);
			return Boolean.FALSE;
		}
	}

	@Override
	public void printMessage(final String message, final boolean isError) {
		out.submit(new Runnable() {
			@Override
			public void run() {
				debug("printMessage=" + message);
				if (view != null)
					view.printMessage(message, isError);
			}
		});
	}

	@Override
	public void setOutDir(final File selectedDir) {
		in.submit(new Runnable() {
			@Override
			public void run() {
				debug("setOutDir=" + selectedDir);
				if (model != null)
					model.setOutDir(selectedDir);
				else
					LOGGER.fatal("model not initialized jet",
							new NullPointerException(
									"model not initialized jet"));
			}
		});
	}

	@Override
	public void showError(final String message) {
		out.submit(new Runnable() {
			@Override
			public void run() {
				debug("showError=" + message);
				if (view != null)
					view.showError(message);
			}
		});
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure(Preferences.LOG_PROPERTIES);
		debug("starting up");
		final String m = StringUtils.getString("System properties:", Preferences.NEW_LINE, "\t", System.getProperties());
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
			final String m2 = StringUtils.getString(Preferences.Controller.SETTINGS_LOADED_GOOD, " (from ", f, ")");
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
