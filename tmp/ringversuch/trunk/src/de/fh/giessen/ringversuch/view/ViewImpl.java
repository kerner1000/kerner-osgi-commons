package de.fh.giessen.ringversuch.view;

import java.awt.Dimension;
import java.io.File;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.controller.Controller;

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
	private final Controller controller;
	private final ViewImplMain panel;
	private final ViewImplSettings panelSettings;
	private JFrame settingsFrame;

	public ViewImpl(final Controller controller) {
		this.controller = controller;
		// Create and set up the content pane.
		// TODO must it be initialized in EventThread?
		panel = new ViewImplMain(this);
		panelSettings = new ViewImplSettings(this);
		setLookAndFeel();
		createMainFrame();
		createSettingsFrame();
	}

	@Override
	public void printMessage(final String message, final boolean isError) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.printMessage(message, isError);
			}
		});
	}

	@Override
	public void showError(final String message) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(panel, message, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	@Override
	public void setOnline() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.setOnline();
			}
		});
	}

	@Override
	public void setWorking() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.setWorking();
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
	public void setSettingsOut(final Properties settings) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		panelSettings.setSettings(settings);
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
				panel.setOpaque(true);
				frame.setContentPane(panel);
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
	public Properties getSettings() {
		return controller.getSettings();
	}

	@Override
	public void setOutDir(File selectedFile) {
		controller.setOutDir(selectedFile);
	}

	@Override
	public void setSelectedFiles(File[] inputFiles) {
		controller.setSelectedFiles(inputFiles);
	}
	
	@Override
	public boolean setSettingsIn(Properties properties){
		return controller.setSettingsIn(properties);
	}

	@Override
	public void start() {
		controller.start();
	}
}
