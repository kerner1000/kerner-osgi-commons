package de.fh.giessen.ringversuch.view;

import java.io.File;
import java.util.Properties;

interface SettingsViewController {

	void showError(String message);

	boolean settingsValid();

	Properties getDefaultSettings();
	
	Properties getSettings();

	void setSettings(Properties settings);

	void saveSettings(File selectedFile);

	void loadSettings(File file);
}
