package de.fh.giessen.ringversuch.view2;

import java.awt.Container;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.controller.ControllerIn;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * @notThreadSave access this class via AWT event thread.
 * @author Alexander Kerner
 *
 */
public class SettingsView implements SwingView {
	
	private final SettingsContent content;
	private final ControllerIn controller;
	private final SwingViewManager manager;
	private final JFrame frame = new JFrame();
	private final JPanel panel = new JPanel();

	public SettingsView(SwingViewManager manager, ControllerIn controller) {
		this.manager = manager;
		this.controller = controller;
		this.content = new SettingsContentImpl(this);
		frame.setTitle(
				Preferences.View.SETTINGS_FRAME_NAME);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// content panes must be opaque
		panel.setOpaque(true);
		frame.setContentPane(panel);
		// Display the window.
		frame.pack();
		frame.setMinimumSize(new Dimension(400, 200));
		frame.setResizable(false);
		frame.setVisible(false);
	}

	@Override
	public void hideView() {
		frame.setVisible(false);
	}

	@Override
	public void showView() {
		frame.setVisible(true);
	}

	@Override
	public ViewTypeSettings getSettings() {
		return content.getSettings();
	}

	@Override
	public void printMessage(String message, boolean isError) {
		throw new IllegalStateException();
	}

	@Override
	public void setOnline() {
		throw new IllegalStateException();
	}

	@Override
	public void setProgress(int current, int max) {
		throw new IllegalStateException();
	}

	@Override
	public void setReady() {
		throw new IllegalStateException();
	}

	@Override
	public void setSettings_view(ViewTypeSettings settings) {
		content.setSettings(settings);
	}

	@Override
	public void setWorking() {
		throw new IllegalStateException();
	}

	@Override
	public void showError(String message) {
		throw new IllegalStateException();
	}

	@Override
	public void cancel() {
		throw new IllegalStateException();
	}

	@Override
	public void detect() {
		throw new IllegalStateException();
	}

	@Override
	public boolean loadSettings(File file) {
		return manager.loadSettings(file);
	}

	@Override
	public boolean saveSettings(ViewTypeSettings settings) {
		return manager.setSettings_controller(settings);
	}

	@Override
	public void setOutDir(File selectedFile) {
		throw new IllegalStateException();
	}

	@Override
	public boolean setSelectedFiles(File[] inputFiles) {
		throw new IllegalStateException();
	}

	@Override
	public boolean setSettings_controller(ViewTypeSettings settings) {
		throw new IllegalStateException();
	}

	@Override
	public void start() {
		throw new IllegalStateException();
	}
	
	public SwingViewManager getSwingViewManager() {
		return manager;
	}
	
	public Container getContainer() {
		return panel;
	}

}
