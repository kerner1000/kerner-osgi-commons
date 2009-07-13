package de.fh.giessen.ringversuch.model;

import java.io.File;


public interface Model {

	void setOutDir(File selectedDir);

	void setSelectedFiles(File[] inputFiles);

	void start();

	SettingsModel getSettings();
	
	void setSettings(SettingsModel settings);

}
