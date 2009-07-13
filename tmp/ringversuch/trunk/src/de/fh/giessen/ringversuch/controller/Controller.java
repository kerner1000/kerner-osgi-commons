package de.fh.giessen.ringversuch.controller;

import java.io.File;

import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.view.SettingsView;
import de.fh.giessen.ringversuch.view.View;

public interface Controller {

	void printMessage(String message, boolean isError);
	void showError(String message);
	void setOutDir(File selectedFile);
	void setSelectedFiles(File[] inputFiles);
	
	// TODO maybe change from "set" to "add" to be able to handle more than one view/model
 	void setView(View view);
	void setModel(Model model);
	
	void start();
	
	void done(boolean b);
	
	/**
	 * from view to model
	 * @param file
	 */
	boolean loadSettings(File file);
	
	/**
	 * from view to model
	 * @param settings
	 */
	boolean saveSettings(SettingsView settings);
	
	/**
	 * from view to model
	 * @param settings
	 */
	boolean setSettings(SettingsView settings);

}
