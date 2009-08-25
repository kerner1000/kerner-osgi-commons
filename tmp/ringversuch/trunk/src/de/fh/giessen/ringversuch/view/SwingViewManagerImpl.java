package de.fh.giessen.ringversuch.view;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.controller.ControllerIn;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>
 * Besides implementing {@link SwingViewManager} functionality, this class takes
 * care of dropping incoming calls to AWT event thread and also escaping
 * outgoing ones from it. AWT event thread issues are centrally handled here, so
 * there is no need to take care of these anywhere else.
 * </p>
 * 
 * @threadSave
 * @author Alexander Kerner
 * @lastVisit 2009-08-26
 * 
 */
public class SwingViewManagerImpl implements SwingViewManager {

	private final static Logger LOGGER = Logger
			.getLogger(SwingViewManagerImpl.class);
	private final Map<ViewType, SwingView> map = new ConcurrentHashMap<ViewType, SwingView>();
	private volatile ViewTypeSettings settings;
	private final ExecutorService exe = Executors.newCachedThreadPool();
	private final ControllerIn controller;

	public SwingViewManagerImpl(ControllerIn controller) {
		this.controller = controller;
		setLookAndFeel();
	}

	@Override
	public void addView(ViewType type, SwingView view) {
		map.put(type, view);
	}

	@Override
	public void hideView() {
		throw new IllegalStateException();
	}

	@Override
	public void showView() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).showView();
			}
		});
	}

	@Override
	public void switchView(final ViewState state) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				switch (state) {
				case NORMAL:
					map.get(ViewType.SETTINGS).hideView();
					break;
				case SETTINGS_ACTIVE:
					map.get(ViewType.SETTINGS).showView();
					break;
				default:
					throw new IllegalStateException("view state " + state
							+ " unknown");
				}
			}
		});
	}

	@Override
	public ViewTypeSettings getSettings() {
		return get();
	}

	@Override
	public void printMessage(final String message, final boolean isError) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).printMessage(message, isError);
			}
		});
	}

	@Override
	public void setOnline() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).setOnline();
			}
		});
	}

	@Override
	public void setProgress(final int current, final int max) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).setProgress(current, max);
			}
		});
	}

	@Override
	public void setReady() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).setReady();
			}
		});
	}

	@Override
	public void setSettings_view(final ViewTypeSettings settings) {
		set(settings);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.SETTINGS).setSettings_view(settings);
			}
		});
	}

	@Override
	public void setWorking() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).setWorking();
			}
		});
	}

	@Override
	public void showError(final String message) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).showError(message);
			}
		});
	}

	@Override
	public void cancel() {
		exe.submit(new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				controller.cancel();
				return null;
			}
		});
	}

	@Override
	public void detect() {
		exe.submit(new Callable<Void>(){
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
			return exe.submit(new Callable<Boolean>(){
				@Override
				public Boolean call() throws Exception {
					return controller.loadSettings(file);
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return false;
		}		
	}

	@Override
	public boolean saveSettings(final ViewTypeSettings settings) {
		try {
			return exe.submit(new Callable<Boolean>(){
				@Override
				public Boolean call() throws Exception {
					return controller.saveSettings(settings);
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return false;
		}		
	}

	@Override
	public void setOutDir(final File selectedFile) {
		exe.submit(new Callable<Void>(){
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
			return exe.submit(new Callable<Boolean>(){
				@Override
				public Boolean call() throws Exception {
					return controller.setSelectedFiles(inputFiles);
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return false;
		}
	}

	@Override
	public boolean setSettings_controller(final ViewTypeSettings settings) {
		try {
			return exe.submit(new Callable<Boolean>(){
				@Override
				public Boolean call() throws Exception {
					return controller.setSettings_controller(settings);
				}
			}).get();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return false;
		}
	}

	@Override
	public void start() {
		exe.submit(new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				controller.start();
				return null;
			}
		});
	}

	private static void setLookAndFeel() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (Preferences.NATIVE_LAF) {
					try {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						LOGGER.warn("could not init native look and feel", e);
					}
				}
			}
		});
	}

	@Override
	public ViewTypeSettings get() {
		return settings;
	}

	@Override
	public void set(ViewTypeSettings settings) {
		this.settings = settings;
	}
}
