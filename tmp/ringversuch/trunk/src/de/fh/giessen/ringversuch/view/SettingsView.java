package de.fh.giessen.ringversuch.view;

import java.awt.Dimension;

import javax.swing.JFrame;

import de.fh.giessen.ringversuch.common.Preferences;

/**
 * @notThreadSave access this class via AWT event thread.
 * @author Alexander Kerner
 * @lastVisit 2009-08-26
 *
 */
public class SettingsView extends AbstractSwingView {
	
	private final SettingsContent content;

	public SettingsView(SwingViewManager manager) {
		super(manager);
		this.content = new SettingsContentImpl(this);
		frame.setTitle(
				Preferences.View.SETTINGS_FRAME_NAME);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// content panes must be opaque
		panel.setOpaque(true);
		// Display the window.
		frame.pack();
		frame.setMinimumSize(new Dimension(400, 200));
		frame.setResizable(false);
		frame.setVisible(false);
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
	public void setWorking() {
		throw new IllegalStateException();
	}

	@Override
	public void showError(String message) {
		throw new IllegalStateException();
	}
}
