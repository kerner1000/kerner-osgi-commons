package de.fh.giessen.ringversuch.view;

import java.awt.Dimension;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.controller.ControllerIn;
import de.fh.giessen.ringversuch.view.settings.ViewSettings;

/**
 * @ThreadSave
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 *
 */
public class ViewImpl implements View, ViewController {

	final static String SETTINGS_FRAME_NAME = "Settings";
	final static String MENU_TITLE = "Menu";
	final static String MENU_ABOUT = "About";
	final static String MENU_SETTINGS = "Settings";
	final static String BUTTON_START = "Start";
	final static String BUTTON_SELECT = "Select files";
	final static String BUTTON_SAVE = "Save in...";
	final static String BUTTON_CANCEL = "Cancel";
	final static String FILES_TITLE = "Files";
	final static String LOG_TITLE = "Log";
	final static String PROGRESS_AND_BUTTONS_TITLE = "Progress";
	private final static Logger LOGGER = Logger.getLogger(ViewImpl.class);
	private final ExecutorService exe = Executors.newSingleThreadExecutor();
	private final ControllerIn controller;
	private final ViewImplMain viewMain;
	private final ViewImplSettings panelSettings;
	private JFrame settingsFrame;

	public ViewImpl(final ControllerIn controller) {
		this.controller = controller;
		// Create and set up the content pane.
		viewMain = new ViewImplMain(this);
		panelSettings = new ViewImplSettings(this);
		setLookAndFeel();
		createMainFrame();
		createSettingsFrame();
	}

	// swing event thread, no need to sync
	@Override
	public void printMessage(final String message, final boolean isError) {
		LOGGER.debug("print message="+message+", is error="+isError);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.printMessage(message, isError);
			}
		});
	}

	// swing event thread, no need to sync
	@Override
	public void showError(final String message) {
		LOGGER.debug("show error="+message);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(viewMain, message, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	@Override
	public void setOnline() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.setOnline();
			}
		});
	}
	
	
	@Override
	public void setReady() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.setReady();
			}
		});
	}

	@Override
	public void setWorking() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.setWorking();
			}
		});
	}
	
	// swing event thread, no need to sync
	@Override
	public ViewSettings getSettings() {
			return	panelSettings.getSettings();
	}

	// swing event thread / panelSettings final, no need to sync
	@Override
	public void setSettings(final ViewSettings settings) {
		LOGGER.debug("new settings="+settings);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panelSettings.setSettings(settings);
			}
		});
	}


	@Override
	public void showSettingsView() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				settingsFrame.setVisible(true);
			}
		});
	}
	
	@Override
	public void hideSettingsView() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				settingsFrame.setVisible(false);
			}
		});
	}
	
	@Override
	public void setProgress(final int current, final int max) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.setProgress(current, max);
			}
		});
	}

	private void createSettingsFrame() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				settingsFrame = new JFrame(SETTINGS_FRAME_NAME);
				settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				// content panes must be opaque
				panelSettings.setOpaque(true);
				settingsFrame.setContentPane(panelSettings);
				// Display the window.
				settingsFrame.pack();
				settingsFrame.setMinimumSize(new Dimension(400, 200));
				settingsFrame.setResizable(false);
				settingsFrame.setVisible(false);
			}
		});
	}

	private void createMainFrame() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JFrame frame = new JFrame(Preferences.NAME + " "
						+ Preferences.VERSION);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// content panes must be opaque
				viewMain.setOpaque(true);
				frame.setContentPane(viewMain);
				// Display the window.
				frame.pack();
				frame.setMinimumSize(new Dimension(400, 200));

				// TODO maybe move to "setOnline()"
				frame.setVisible(true);
			}
		});
	}

	private void setLookAndFeel() {
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
	public void setOutDir(final File selectedFile) {
		// in oder to get away from swing event thread, we create a new one.
		exe.submit(new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				controller.setOutDir(selectedFile);
				return null;
			}
		});
	}

	// synchronization ???
	@Override
	public synchronized boolean setSelectedFiles(File[] inputFiles) {
		return controller.setSelectedFiles(inputFiles);
	}

	@Override
	public synchronized void start() {
		// in oder to get away from swing event thread, we create a new one.
		exe.submit(new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				controller.start();
				return null;
			}
		});
	}
	
	/**
	 * Must not be synchronized. if we uncomment running in thread, cancelling does not work.
	 */
	@Override
	public void cancel() {
//		// in oder to get away from swing event thread, we create a new one.
//		exe.submit(new Callable<Void>(){
//			@Override
//			public Void call() throws Exception {
				controller.cancel();
//				return null;
//			}
//		});
	}
	
	@Override
	public synchronized void detect() {
		// in oder to get away from swing event thread, we create a new one.
		exe.submit(new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				controller.detect();
				return null;
			}
		});
	}

	// synchronization ???
	@Override
	public synchronized boolean loadSettings(File file) {
		return controller.loadSettings(file);
	}

	// synchronization ???
	@Override
	public synchronized boolean saveSettingsOut(ViewSettings settings) {
		return controller.saveSettings(settings);
	}

	// synchronization ???
	@Override
	public synchronized boolean setSettingsOut(ViewSettings settings) {
		return controller.setSettings(settings);
	}

}
