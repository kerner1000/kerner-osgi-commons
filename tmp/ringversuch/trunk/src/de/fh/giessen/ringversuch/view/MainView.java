package de.fh.giessen.ringversuch.view;

import java.awt.Dimension;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

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
	public void outgoingPrintMessage(String message, boolean isError) {
		content.printMessage(message, isError);
	}

	@Override
	public void outgoingSetOnline() {
		content.setOnline();
	}

	@Override
	public void outgoingSetProgress(int current, int max) {
		content.setProgress(current, max);
	}

	@Override
	public void outgoingSetReady() {
		content.setReady();
	}

	@Override
	public void outgoingSetWorking() {
		content.setWorking();
	}

	@Override
	public void outgoingShowError(String message) {
		content.showError(message);
	}

	@Override
	public void outgoingSetSettings(ViewTypeSettings settings) {
		throw new IllegalStateException();
	}
}
