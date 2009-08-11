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
import de.fh.giessen.ringversuch.view.View;
import de.fh.giessen.ringversuch.view.ViewImpl;
import de.fh.giessen.ringversuch.view.settings.ViewSettings;
import de.fh.giessen.ringversuch.view.settings.ViewSettingsImpl;

class ControllerImpl implements Controller {

	private final static Logger LOGGER = Logger.getLogger(ControllerImpl.class);
	private final ExecutorService out = Executors.newCachedThreadPool();
	private final ExecutorService in = Executors.newCachedThreadPool();
	private View view;
	private Model model;

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
	public void setSelectedFiles(final File[] inputFiles) {
		in.submit(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("setSelectedFiles=" + inputFiles);
				if (model != null)
					model.setSelectedFiles(inputFiles);
				else
					LOGGER.fatal("model not initialized jet",
							new NullPointerException(
									"model not initialized jet"));
			}
		});
		LOGGER.info("files selected, trying to detect settings");
		detect();
	}

	@Override
	public void start() {
		in.submit(new Runnable() {
			@Override
			public void run() {
				try {
					view.setWorking();
					model.start();
				} catch (CancellationException e) {
					LOGGER.info("work cancelled", e);
					view.printMessage("work cancelled", false);
					return;
				} catch (Exception e) {
					LOGGER.error(e.getLocalizedMessage(), e);
					view.showError("failed! (" + e.getLocalizedMessage() + ")");
					view.printMessage("failed! (" + e.getLocalizedMessage() + ")", true);
				} finally {
					// setting view to "online" instead of "ready" because of
					// errors is maybe a good idea
					view.setReady();
				}
			}
		});
	}

	@Override
	public void cancel() {
		in.submit(new Runnable() {
			@Override
			public void run() {
				LOGGER.debug("cancelling");
				model.cancel();
			}
		});
	}

	@Override
	public void done(boolean b) {
		out.submit(new Runnable() {
			@Override
			public void run() {
				LOGGER.debug("done. setting view ready.");
				view.setReady();
			}
		});
	}

	@Override
	public void setProgress(final int current, final int max) {
		out.submit(new Runnable() {
			@Override
			public void run() {
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
					return Boolean.TRUE;
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError("could not load settings. ("
					+ e.getLocalizedMessage() + ")");
			return Boolean.FALSE;
		}
	}

	@Override
	public synchronized void detect() {
		LOGGER.debug("auto-detecting settings");
		detectProbeCell();
		view.setSettings(SettingsConverter.modelSettingsToViewSettings(model
				.getSettings()));
		detectLaborCell();
		view.setSettings(SettingsConverter.modelSettingsToViewSettings(model
				.getSettings()));
		detectColumnOfSubstances();
		view.setSettings(SettingsConverter.modelSettingsToViewSettings(model
				.getSettings()));
		detectValuesBeginCell();
		view.setSettings(SettingsConverter.modelSettingsToViewSettings(model
				.getSettings()));
		detectValuesEndCell();
		view.setSettings(SettingsConverter.modelSettingsToViewSettings(model
				.getSettings()));
	}

	private void detectColumnOfSubstances() {
		LOGGER.debug("auto-detecting settings column of substances");
		try {
			in.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					model.detectColumnOfSubstances();
					return null;
				}
			}).get();
			LOGGER.debug("auto-detecting settings column of substances successful");
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.printMessage("could not find column of substances ("
					+ e.getLocalizedMessage() + ")", true);
		}
	}

	private void detectValuesEndCell() {
		LOGGER.debug("auto-detecting settings values end cell");
		try {
			in.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					model.detectValuesEndCell();
					return null;
				}
			}).get();
			LOGGER.debug("auto-detecting settings values end cell successful");
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.printMessage("could not find values end cell ("
					+ e.getLocalizedMessage() + ")", true);
		}
	}

	private void detectValuesBeginCell() {
		LOGGER.debug("auto-detecting settings values begin cell");
		try {
			in.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					model.detectValuesBeginCell();
					return null;
				}
			}).get();
			LOGGER
					.debug("auto-detecting settings values begin cell successful");
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.printMessage("could not find values begin cell ("
					+ e.getLocalizedMessage() + ")", true);
		}
	}

	private void detectLaborCell() {
		LOGGER.debug("auto-detecting settings labor cell");
		try {
			in.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					model.detectLaborCell();
					return null;
				}
			}).get();
			LOGGER.debug("auto-detecting settings labor cell successful");
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.printMessage("could not find labor cell ("
					+ e.getLocalizedMessage() + ")", true);
		}
	}

	private void detectProbeCell() {
		LOGGER.debug("auto-detecting settings probe cell");
		try {
			in.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					model.detectProbeCell();
					return null;
				}
			}).get();
			LOGGER.debug("auto-detecting settings probe cell successful");
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.printMessage("could not find probe cell ("
					+ e.getLocalizedMessage() + ")", true);
		}
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
					return Boolean.TRUE;
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError("could not save settings. ("
					+ e.getLocalizedMessage() + ")");
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean setSettings(final ViewSettings settings) {
		try {
			return in.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					LOGGER.debug("setting settings");
					model.setSettings(SettingsConverter
							.viewSettingsToModelSettings(settings));
					LOGGER.debug("settings successfull set");
					return Boolean.TRUE;
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			view.showError("could not set settings. ("
					+ e.getLocalizedMessage() + ")");
			return Boolean.FALSE;
		}
	}

	@Override
	public void printMessage(final String message, final boolean isError) {
		out.submit(new Runnable() {
			@Override
			public void run() {
				LOGGER.debug("printMessage=" + message);
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
				LOGGER.debug("setOutDir=" + selectedDir);
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
				LOGGER.debug("showError=" + message);
				if (view != null)
					view.showError(message);
			}
		});
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
			view.setSettings(SettingsConverter
					.modelSettingsToViewSettings(model.getSettings()));
			final String message = "successfully loaded settings from " + f;
			LOGGER.info(message);
			controller.printMessage(message, false);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			controller.printMessage(e.getLocalizedMessage(), false);
			controller.printMessage("loading default settings", false);
			view.setSettings(new ViewSettingsImpl());
		}

		view.setOnline();
		LOGGER.debug("view created and online");
	}

}
