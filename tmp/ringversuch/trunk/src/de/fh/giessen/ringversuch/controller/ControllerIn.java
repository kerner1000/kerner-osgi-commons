package de.fh.giessen.ringversuch.controller;

import java.io.File;

import de.fh.giessen.ringversuch.view.settings.ViewSettings;

/**
 * 
 * <p>Handle Events from View to Model.</p>
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-08-14
 *
 */
public interface ControllerIn {
	
	/**
	 * <p>In</p>
	 * <p>Notifies about an incoming cancel request.</p>
	 */
	void cancel();
	
	/**
	 * <p>In</p>
	 * <p>Sets new incoming settings.</p>
	 * @param settings
	 * @return true, if settings have been accepted by model, false otherwise.
	 */
	boolean setSettings(ViewSettings settings);
	
	/**
	 * <p>In</p>
	 * <p> Tells the model to save current settings.</p>
	 * @param settings
	 * @return true, if successful, false otherwise.
	 */
	boolean saveSettings(ViewSettings settings);
	
	/**
	 * <p>In</p>
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
