package de.fh.giessen.ringversuch.view;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.view.main.ViewMain;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

class ViewInImpl implements ViewIn {
	
	private static Logger LOGGER = Logger.getLogger(ViewInImpl.class);
	private final CentralViewService central;
	private final ViewMain viewMain;
	
	ViewInImpl(ViewMain viewMain, CentralViewService central){
		this.central = central;
		this.viewMain = viewMain;
	}

	@Override
	public ViewTypeSettings getSettings() {
		return central.getSettings();
	}

	@Override
	public void printMessage(final String message, final boolean isError) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.printMessage(message, isError);
			}
		});
	}

	@Override
	public void setOnline() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.setOnline();
			}
		});
	}

	@Override
	public void setProgress(final int current, final int max) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.setProgress(current, max);
			}
		});
	}

	@Override
	public void setReady() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.setReady();
			}
		});
	}

	@Override
	public void setSettings_view(ViewTypeSettings s) {
		central.setSettings(s);
	}

	@Override
	public void setWorking() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				viewMain.setWorking();
			}
		});
	}

	@Override
	public void showError(final String message) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LOGGER.debug("show error=" + message);
				JOptionPane.showMessageDialog(central.getMainComponent(), message,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}	
}
