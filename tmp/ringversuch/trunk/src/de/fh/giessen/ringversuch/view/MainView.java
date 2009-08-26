package de.fh.giessen.ringversuch.view;

import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * @notThreadSave access this class via AWT event thread.
 * @author Alexander Kerner
 * 
 */
public class MainView extends AbstractSwingView {
	
	protected class F extends JFrame {
		
		private static final long serialVersionUID = -11222207085365360L;

		protected void processWindowEvent(WindowEvent e) {
			if (e.getID() == WindowEvent.WINDOW_CLOSING) {
				ViewUtils.dropToEventThread(new Runnable() {
					public void run() {
						int exit = JOptionPane.showConfirmDialog(frame,
								Preferences.View.CONFIRM_QUIT_TEXT,
								Preferences.View.CONFIRM_QUIT_TITLE,
								JOptionPane.OK_OPTION);
						if (exit == JOptionPane.YES_OPTION) {
							incomingShutdown();
						}
					}
				});
			}
		}
	}

	private final MainContent content;

	public MainView(SwingViewManager manager) {
		super(manager);
		
		// override original Frame, because we want a custom closing behavior
		frame = new F();
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
