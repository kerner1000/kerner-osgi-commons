package de.fh.giessen.ringversuch.view2;

import java.awt.Component;
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
public class MainView implements SwingView {

	private final MainContent content;
	private final ControllerIn controller;
	private final SwingViewManager manager;
	private final JFrame frame;
	private final JPanel panel;

	public MainView(SwingViewManager manager, ControllerIn controller) {
		panel = new JPanel();
		frame = new JFrame();
		this.manager = manager;
		this.controller = controller;
		content = new MainContentImpl(this);
		frame.setTitle(Preferences.NAME + " " + Preferences.VERSION);
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		frame.setMinimumSize(new Dimension(400, 200));
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
		return manager.getSettings();
	}

	@Override
	public void printMessage(String message, boolean isError) {
		content.printMessage(message, isError);
	}

	@Override
	public void setOnline() {
		content.setOnline();
	}

	@Override
	public void setProgress(int current, int max) {
		content.setProgress(current, max);
	}

	@Override
	public void setReady() {
		content.setReady();
	}

	@Override
	public void setSettings_view(ViewTypeSettings settings) {
		throw new IllegalStateException();
	}

	@Override
	public void setWorking() {
		content.setWorking();
	}

	@Override
	public void showError(String message) {
		content.showError(message);
	}

	@Override
	public void cancel() {
		controller.cancel();
	}

	@Override
	public void detect() {
		controller.detect();
	}

	@Override
	public boolean loadSettings(File file) {
		return controller.loadSettings(file);
	}

	@Override
	public boolean saveSettings(ViewTypeSettings settings) {
		return controller.saveSettings(settings);
	}

	@Override
	public void setOutDir(File selectedFile) {
		controller.setOutDir(selectedFile);
	}

	@Override
	public boolean setSelectedFiles(File[] inputFiles) {
		return controller.setSelectedFiles(inputFiles);
	}

	@Override
	public boolean setSettings_controller(ViewTypeSettings settings) {
		return controller.setSettings_controller(settings);
	}

	@Override
	public void start() {
		controller.start();
	}

	public Container getContainer() {
		return panel;
	}

	public SwingViewManager getSwingViewManager() {
		return manager;
	}
}
