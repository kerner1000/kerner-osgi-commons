package de.fh.giessen.ringversuch.view;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.controller.ControllerIn;
import de.fh.giessen.ringversuch.view.main.ViewMain;
import de.fh.giessen.ringversuch.view.main.ViewMainImpl;
import de.fh.giessen.ringversuch.view.settings.ViewSettingsImpl;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

// ViewImpl must take care of threading (delegating to awt event thread)
/**
 * @ThreadSave
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 * 
 */
public class ViewImpl implements View {

	private final static Logger LOGGER = Logger.getLogger(ViewImpl.class);
	private final ViewIn viewIn;
	private final ViewOut viewOut;
	private final CentralViewService central;
	private final ViewMain viewMain;
	// private final ViewSettingsv viewSettings;
	private final Component mainComponent;
	private final JPanel mainPanel;
	private final JPanel settingsComponent;
	private final JPanel settingsPanel;
	private volatile ViewTypeSettings settings;;

	public ViewImpl(final ControllerIn controller) {
		setLookAndFeel();
		createMainFrame();
		createSettingsFrame();
		viewIn = new ViewInImpl(viewMain, central);
		viewOut = new ViewOutImpl(controller, viewIn);
		central = new CentralViewServiceImpl(settingsPanel, mainPanel);

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

	private void createMainFrame() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JFrame frame = new JFrame(Preferences.NAME + " "
						+ Preferences.VERSION);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// content panes must be opaque
				mainPanel.setOpaque(true);
				frame.setContentPane(mainPanel);
				// Display the window.
				frame.pack();
				frame.setMinimumSize(new Dimension(400, 200));

				// TODO maybe move to "setOnline()"
				frame.setVisible(true);
			}
		});
	}

	private void createSettingsFrame() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JFrame frame = new JFrame(
						Preferences.View.SETTINGS_FRAME_NAME);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				// content panes must be opaque
				settingsPanel.setOpaque(true);
				frame.setContentPane(settingsPanel);
				// Display the window.
				frame.pack();
				frame.setMinimumSize(new Dimension(400, 200));
				frame.setResizable(false);
				frame.setVisible(false);
			}
		});
	}

	@Override
	public boolean loadSettings(File file) {
		return viewOut.loadSettings(file);
	}

	@Override
	public boolean saveSettings(ViewTypeSettings settings) {
		return viewOut.saveSettings(settings);
	}

	@Override
	public void setOutDir(final File selectedFile) {
		viewOut.setOutDir(selectedFile);
	}

	@Override
	public boolean setSelectedFiles(File[] inputFiles) {
		return viewOut.setSelectedFiles(inputFiles);
	}

	@Override
	public void start() {
		viewOut.start();
	}

	/**
	 * Must not be synchronized. if we uncomment running in thread, cancelling
	 * does not work.
	 */
	@Override
	public void cancel() {
		viewOut.cancel();
	}

	@Override
	public void detect() {
		viewOut.detect();
	}

	@Override
	public boolean setSettings_controller(ViewTypeSettings settings) {
		return viewOut.setSettings_controller(settings);
	}

	@Override
	public void setSettings_view(ViewTypeSettings settings) {
		viewIn.setSettings_view(settings);
	}

	@Override
	public void setProgress(final int current, final int max) {
		viewIn.setProgress(current, max);
	}

	@Override
	public void printMessage(final String message, final boolean isError) {
		viewIn.printMessage(message, isError);
	}

	@Override
	public void showError(final String message) {
		viewIn.showError(message);
	}

	@Override
	public void setOnline() {
		viewIn.setOnline();
	}

	@Override
	public void setReady() {
		viewIn.setReady();
	}

	@Override
	public void setWorking() {
		viewIn.setWorking();
	}

	@Override
	public ViewTypeSettings getSettings() {
		return central.getSettings();
	}
}
