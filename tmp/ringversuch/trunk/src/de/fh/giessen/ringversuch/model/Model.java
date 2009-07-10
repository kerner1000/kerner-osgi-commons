package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.Properties;

public interface Model {

	void setOutDir(File selectedDir);

	void setSelectedFiles(File[] inputFiles);
	
	boolean loadSettings(File settingsFile);
	
	boolean saveSettings();

	void start();

	Properties getDefaultSettings();
	
	Properties getCurrentSettings();

	boolean setSettings(Properties settings);

}
