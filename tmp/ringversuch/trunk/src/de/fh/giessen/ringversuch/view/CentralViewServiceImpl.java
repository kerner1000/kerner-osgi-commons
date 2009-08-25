package de.fh.giessen.ringversuch.view;

import java.awt.Component;

import javax.swing.JPanel;

import de.fh.giessen.ringversuch.view.main.ViewMain;
import de.fh.giessen.ringversuch.view.main.ViewMainImpl;
import de.fh.giessen.ringversuch.view.settings.ViewSettings;
import de.fh.giessen.ringversuch.view.settings.ViewSettingsImpl;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettingsImpl;

public class CentralViewServiceImpl implements CentralViewService {
	
	private ViewTypeSettings settings = new ViewTypeSettingsImpl();
	private final JPanel settingsPanel;
	private final JPanel mainPanel;
	private final ViewMain viewMain;
	private final ViewSettings viewSettings;
	
	public CentralViewServiceImpl() {
		ViewMainImpl chicken = new ViewMainImpl(viewOut, this);
		viewMain = chicken;
		mainPanel = chicken;
		ViewSettingsImpl cow = new ViewSettingsImpl(viewOut, this);
		settingsPanel = cow;
		this.viewSettings = new ViewSettingsImpl(viewOut, this);
	}
	
	@Override
	public synchronized ViewTypeSettings getSettings() {
		return settings;
	}

	@Override
	public synchronized void hideSettingsView() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				settingsPanel.setVisible(false);
			}
		});
	}

	@Override
	public synchronized void showSettingsView() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				settingsPanel.setVisible(true);
			}
		});
	}

	@Override
	public synchronized void setSettings(ViewTypeSettings s) {
		settings = s;
	}

	@Override
	public synchronized Component getMainComponent() {
		return mainPanel;
	}

}
