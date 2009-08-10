package de.fh.giessen.ringversuch.controller;

import java.io.File;

import de.fh.giessen.ringversuch.view.settings.ViewSettings;

/**
 * 
 * Handle Events from View to Model
 * 
 * @author Alexander Kerner
 *
 */
public interface ControllerIn {
	
	/**
	 * <p>In</p>
	 * <p>Tell the model to cancel<p> 
	 * 
	 */
	void cancel();
	
	/**
	 * In
	 * @param settings
	 * @return
	 */
	boolean setSettings(ViewSettings settings);
	
	/**
	 * In
	 * @param settings
	 * @return
	 */
	boolean saveSettings(ViewSettings settings);
	
	/**
	 * In
	 * @param file
	 * @return
	 */
	boolean loadSettings(File file);
	
	/**
	 * In
	 */
	void start();
	
	/**
	 * In
	 * @param selectedFile
	 */
	void setOutDir(File selectedFile);
	
	/**
	 * In
	 * @param inputFiles
	 */
	void setSelectedFiles(File[] inputFiles);

	/**
	 * In
	 */
	void detect();

}
