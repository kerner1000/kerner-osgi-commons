package de.fh.giessen.ringversuch.controller;

import java.io.File;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * 
 * <p>
 * Handle Events from View to Model.
 * </p>
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-08-26
 * 
 */
public interface ControllerIn {

	/**
	 * <p>
	 * from View to Model
	 * </p>
	 */
	void incomingCancel();

	// Must return a boolean value, so that view implementation does know if the
	// current window may be closed.
	/**
	 * <p>
	 * from View to Model
	 * </p>
	 * 
	 */
	void incomingSetSettings(ViewTypeSettings settings);

	// Must return a boolean value, so that view implementation does know if the
	// current window may be closed.
	/**
	 * <p>
	 * from View to Model
	 * </p>
	 * 
	 */
	void incomingSaveSettings(ViewTypeSettings settings);

	// Must return a boolean value, so that view implementation does know if the
	// current window may be closed.
	/**
	 * /**
	 * <p>
	 * from View to Model
	 * </p>
	 * 
	 */
	void incomingLoadSettings(File file);

	/**
	 * <p>
	 * from View to Model
	 * </p>
	 */
	void incomingStart();

	/**
	 * <p>
	 * from View to Model
	 * </p>
	 */
	void incomingShutdown();

	/**
	 * /**
	 * <p>
	 * from View to Model
	 * </p>
	 * 
	 * @param selectedFile
	 *            directory to store files in
	 */
	void incomingSetOutDir(File selectedFile);

	// Must return a boolean value, so that view implementation does know if it
	// must update the file list.
	/**
	 * /**
	 * <p>
	 * from View to Model
	 * </p>
	 * 
	 */
	void incomingSetSelectedFiles(File[] inputFiles);

	/**
	 * <p>
	 * from View to Model
	 * </p>
	 */
	void incomingDetect();

}
