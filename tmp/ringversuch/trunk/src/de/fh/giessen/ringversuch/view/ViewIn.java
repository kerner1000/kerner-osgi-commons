package de.fh.giessen.ringversuch.view;

import java.io.File;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>Handle incoming events (from View bzw. User to Model bzw. Controller).</p>
 * @author Alexander Kerner
 * @lastVisit 2009-08-26
 *
 */
public interface ViewIn {
	
	/**
	 * From View to Model
	 */
	void incomingCancel();
	
	/**
	 * From View to Model
	 */
	boolean incomingSetSettings(ViewTypeSettings settings);
	
	/**
	 * From View to Model
	 */
	boolean incomingSaveSettings(ViewTypeSettings settings);
	
	/**
	 * From View to Model
	 */
	boolean incomingLoadSettings(File file);
	
	/**
	 * From View to Model
	 */
	void incomingStart();
	
	/**
	 * From View to Model
	 */
	void incomingShutdown();
	
	/**
	 * From View to Model
	 */
	void incomingSetOutDir(File selectedFile);
	
	/**
	 * From View to Model
	 */
	boolean incomingSetSelectedFiles(File[] inputFiles);
	
	/**
	 * From View to Model
	 */
	void incomingDetect();

}
