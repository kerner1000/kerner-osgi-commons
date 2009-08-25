package de.fh.giessen.ringversuch.controller;

import java.io.File;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

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
	
	//Must return a boolean value, so that view implementation does know if the current window may be closed.
	/**
	 * <p>In</p>
	 * <p>Sets new incoming settings.</p>
	 * @param settings
	 * @return true, if settings have been accepted by model, false otherwise.
	 */
	boolean setSettings_controller(ViewTypeSettings settings);
	
	//Must return a boolean value, so that view implementation does know if the current window may be closed.
	/**
	 * <p>In</p>
	 * <p> Tells the model to save current settings.</p>
	 * @param settings
	 * @return true, if successful, false otherwise.
	 */
	boolean saveSettings(ViewTypeSettings settings);
	
	//Must return a boolean value, so that view implementation does know if the current window may be closed.
	/**
	 * <p>In</p>
	 * <p> Tells the model to load settings from a file </p>
	 * @param file
	 * @return true, if successful, false otherwise.
	 */
	boolean loadSettings(File file);
	
	/**
	 * <p>In</p>
	 * <p> Tells the model to start the job </p>
	 */
	void start();
	
	/**
	 * <p> In </p>
	 * <p> Sets the directory to which out files will be written </p>
	 * @param selectedFile directory to store files in
	 */
	void setOutDir(File selectedFile);
	
	//Must return a boolean value, so that view implementation does know if it must update the file list.
	/**
	 * <p> In </p>
	 * <p> Sets input files </p>
	 * @param inputFiles array of input files
	 */
	boolean setSelectedFiles(File[] inputFiles);

	/**
	 * <p> In </p>
	 * <p> Tries to detect the correct settings. </p>
	 */
	void detect();

}
