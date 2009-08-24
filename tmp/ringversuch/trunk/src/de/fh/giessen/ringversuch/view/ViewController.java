package de.fh.giessen.ringversuch.view;

import java.io.File;

import de.fh.giessen.ringversuch.view.settings.ViewSettings;

interface ViewController {

	boolean setSelectedFiles(File[] inputFiles);

	void setOutDir(File selectedFile);

	void start();
	
	void cancel();

	void showSettingsView();
	
	ViewSettings getSettings();
	
	boolean setSettingsOut(ViewSettings settings);

	boolean saveSettingsOut(ViewSettings settings);

	boolean loadSettings(File file);

	void hideSettingsView();

	void detect();
}
