package de.fh.giessen.ringversuch.view;

import java.awt.Dimension;

import de.fh.giessen.ringversuch.common.Preferences;

/**
 * @notThreadSave access this class via AWT event thread.
 * @author Alexander Kerner
 * 
 */
public class MainView extends AbstractSwingView {

	private final MainContent content;

	public MainView(SwingViewManager manager) {
		super(manager);
		content = new MainContentImpl(this);
		frame.setTitle(Preferences.NAME + " " + Preferences.VERSION);
		panel.setOpaque(true);
		frame.pack();
		frame.setMinimumSize(new Dimension(400, 200));
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
	public void setWorking() {
		content.setWorking();
	}

	@Override
	public void showError(String message) {
		content.showError(message);
	}
}
