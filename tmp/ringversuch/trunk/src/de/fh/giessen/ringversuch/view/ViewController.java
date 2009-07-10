package de.fh.giessen.ringversuch.view;

import java.io.File;
import java.util.Properties;

interface ViewController {

	void setSelectedFiles(File[] inputFiles);

	void setOutDir(File selectedFile);

	void start();

	Properties getSettings();

	void showSettingsView();

	boolean setSettingsIn(Properties settings);
	
	void setSettingsOut(Properties settings);

}
