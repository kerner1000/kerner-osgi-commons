package de.fh.giessen.ringversuch.view;

import java.awt.Dimension;

import javax.swing.JFrame;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

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
	public void outgoingSetSettings(ViewTypeSettings settings) {
		content.setSettings(settings);
	}

	@Override
	public void outgoingPrintMessage(String message, boolean isError) {
		throw new IllegalStateException();
	}

	@Override
	public void outgoingSetOnline() {
		throw new IllegalStateException();
	}

	@Override
	public void outgoingSetProgress(int current, int max) {
		throw new IllegalStateException();
	}

	@Override
	public void outgoingSetReady() {
		throw new IllegalStateException();
	}

	@Override
	public void outgoingSetWorking() {
		throw new IllegalStateException();
	}

	@Override
	public void outgoingShowError(String message) {
		throw new IllegalStateException();
	}
}
