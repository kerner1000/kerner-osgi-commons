package de.fh.giessen.ringversuch.view;

import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * @notThreadSave access this class via AWT event thread.
 * @author Alexander Kerner
 * @lastVisit 2009-08-25
 * 
 */
public abstract class AbstractSwingView implements SwingView {
	
	protected final SwingViewManager manager;
	protected final JFrame frame;
	protected final JPanel panel;
	
	public AbstractSwingView(SwingViewManager manager) {
		this.manager = manager;
		frame = new JFrame();
		panel = new JPanel();
		frame.setContentPane(panel);
	}
	
	public SwingViewManager getViewManager(){
		return manager;
	}

	public Container getContainer(){
		return frame;
	}
	
	public JPanel getContent(){
		return panel;
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
	public void setSettings_view(ViewTypeSettings settings) {
		manager.setSettings_view(settings);
	}
	
	@Override
	public void cancel() {
		manager.cancel();
	}

	@Override
	public void detect() {
		manager.detect();
	}

	@Override
	public boolean loadSettings(File file) {
		return manager.loadSettings(file);
	}

	@Override
	public boolean saveSettings(ViewTypeSettings settings) {
		return manager.saveSettings(settings);
	}

	@Override
	public void setOutDir(File selectedFile) {
		manager.setOutDir(selectedFile);
	}

	@Override
	public boolean setSelectedFiles(File[] inputFiles) {
		return manager.setSelectedFiles(inputFiles);
	}

	@Override
	public boolean setSettings_controller(ViewTypeSettings settings) {
		return manager.setSettings_controller(settings);
	}

	@Override
	public void start() {
		manager.start();
	}
}