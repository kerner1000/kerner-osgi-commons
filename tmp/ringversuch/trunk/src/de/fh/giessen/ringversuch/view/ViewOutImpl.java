package de.fh.giessen.ringversuch.view;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;


import de.fh.giessen.ringversuch.controller.ControllerIn;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

class ViewOutImpl implements ViewOut {

	// to be sure that GUI stays responsible, we escape from awt event thread.

	private final static Logger LOGGER = Logger.getLogger(ViewOutImpl.class);
	private final ExecutorService exe = Executors.newCachedThreadPool();
	private final ControllerIn controller;
	private final ViewIn viewIn;

	ViewOutImpl(ControllerIn controller, ViewIn viewIn) {
		this.controller = controller;
		this.viewIn = viewIn;
	}

	@Override
	public void cancel() {
		exe.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				controller.cancel();
				return null;
			}
		});
	}

	@Override
	public void detect() {
		exe.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				controller.detect();
				return null;
			}
		});
	}

	@Override
	public boolean loadSettings(final File file) {
		try {
			return exe.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return controller.loadSettings(file);
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			viewIn.printMessage(e.getLocalizedMessage(), true);
			viewIn.showError(e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public boolean saveSettings(final ViewTypeSettings settings) {
		try {
			return exe.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return controller.saveSettings(settings);
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			viewIn.printMessage(e.getLocalizedMessage(), true);
			viewIn.showError(e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public void setOutDir(final File selectedFile) {
		exe.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				controller.setOutDir(selectedFile);
				return null;
			}
		});
	}

	@Override
	public boolean setSelectedFiles(final File[] inputFiles) {
		try {
			return exe.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return controller.setSelectedFiles(inputFiles);
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			viewIn.printMessage(e.getLocalizedMessage(), true);
			viewIn.showError(e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public boolean setSettings_controller(final ViewTypeSettings settings) {
		try {
			return exe.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return controller.setSettings_controller(settings);
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			viewIn.printMessage(e.getLocalizedMessage(), true);
			viewIn.showError(e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public void start() {
		exe.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				controller.start();
				return null;
			}
		});
	}

}
