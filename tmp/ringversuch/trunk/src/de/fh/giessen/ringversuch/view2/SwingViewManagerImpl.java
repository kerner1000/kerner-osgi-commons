package de.fh.giessen.ringversuch.view2;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettingsImpl;

public class SwingViewManagerImpl implements SwingViewManager {

	private final static Logger LOGGER = Logger
			.getLogger(SwingViewManagerImpl.class);
	private final Map<ViewType, SwingView> map = new ConcurrentHashMap<ViewType, SwingView>();
	private volatile ViewTypeSettings settings;

	public SwingViewManagerImpl() {
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
	public void setSettings_view(ViewTypeSettings settings) {
		set(settings);
		map.get(ViewType.SETTINGS).setSettings_view(settings);
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
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).cancel();
			}
		});
	}

	@Override
	public void detect() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).detect();
			}
		});
	}

	@Override
	public boolean loadSettings(File file) {
		// TODO: did not escape from event thread.
		return map.get(ViewType.MAIN).loadSettings(file);
	}

	@Override
	public boolean saveSettings(final ViewTypeSettings settings) {
		// TODO: did not escape from event thread.
		return map.get(ViewType.MAIN).saveSettings(settings);	
	}

	@Override
	public void setOutDir(final File selectedFile) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).setOutDir(selectedFile);
			}
		});
	}

	@Override
	public boolean setSelectedFiles(File[] inputFiles) {
		// TODO: did not escape from event thread.
		return map.get(ViewType.MAIN).setSelectedFiles(inputFiles);
	}

	@Override
	public boolean setSettings_controller(ViewTypeSettings settings) {
		// TODO: did not escape from event thread.
		return map.get(ViewType.MAIN).setSettings_controller(settings);
	}

	@Override
	public void start() {
		// TODO: did not escape from event thread.
		map.get(ViewType.MAIN).start();
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
