package de.fh.giessen.ringversuch.view;

import java.io.File;

interface ViewController {

	void setSelectedFiles(File[] inputFiles);

	void setOutDir(File selectedFile);

	void start();
	
	void cancel();

	void showSettingsView();
	
	SettingsView getSettings();
	
	boolean setSettingsOut(SettingsView settings);

	boolean saveSettingsOut(SettingsView settings);

	boolean loadSettings(File file);

	void hideSettingsView();
}
