package de.fh.giessen.ringversuch.view;

import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>
 * This class implements all methods from {@link ViewIn} and redirects them to
 * {@link SwingViewManager}.
 * </p>
 * <p>
 * Methods from {@link ViewOut} are left to implement by extending class.
 * </p>
 * 
 * @notThreadSave access this class via AWT event thread.
 * @author Alexander Kerner
 * @lastVisit 2009-08-25
 * 
 */
public abstract class AbstractSwingView implements SwingView {

	protected final SwingViewManager manager;
	// must not be final, because MainView overrides it to implement custom
	// closing behavior
	protected JFrame frame;
	protected final JPanel panel;

	public AbstractSwingView(SwingViewManager manager) {
		this.manager = manager;
		frame = new JFrame();
		panel = new JPanel();
		panel.setOpaque(true);
		frame.setContentPane(panel);
	}

	public SwingViewManager getViewManager() {
		return manager;
	}

	public JPanel getContent() {
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
	public void destroyView() {
		frame.dispose();
	}

	// method is from ViewIn, but since settings are handled in ViewManager, no
	// need to let subclasses implement this method.
	@Override
	public ViewTypeSettings outgoingSetSettings() {
		return manager.outgoingSetSettings();
	}

	@Override
	public void incomingCancel() {
		manager.incomingCancel();
	}

	@Override
	public void incomingDetect() {
		manager.incomingDetect();
	}

	@Override
	public void incomingLoadSettings(File file) {
		manager.incomingLoadSettings(file);
	}

	@Override
	public void incomingSaveSettings(ViewTypeSettings settings) {
		manager.incomingSaveSettings(settings);
	}

	@Override
	public void incomingSetOutDir(File selectedFile) {
		manager.incomingSetOutDir(selectedFile);
	}

	@Override
	public void incomingSetSelectedFiles(File[] inputFiles) {
		manager.incomingSetSelectedFiles(inputFiles);
	}

	@Override
	public void incomingSetSettings(ViewTypeSettings settings) {
		manager.incomingSetSettings(settings);
	}

	@Override
	public void incomingStart() {
		manager.incomingStart();
	}

	@Override
	public void incomingShutdown() {
		manager.incomingShutdown();
	}

	@Override
	public void outgoingShutdown() {
		// At this point, ViewManager should already have shut down the gui.
		throw new IllegalStateException();
	}
}
