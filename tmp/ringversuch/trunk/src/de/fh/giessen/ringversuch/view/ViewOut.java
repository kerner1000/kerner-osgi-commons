package de.fh.giessen.ringversuch.view;

import java.io.File;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>Handle outgoing events (from View bzw. User to Model bzw. Controller).</p>
 * @author Alexander Kerner
 *
 */
public interface ViewOut {
	
	/**
	 * From View to Model
	 */
	void cancel();
	
	/**
	 * From View to Model
	 */
	boolean setSettings_controller(ViewTypeSettings settings);
	
	/**
	 * From View to Model
	 */
	boolean saveSettings(ViewTypeSettings settings);
	
	/**
	 * From View to Model
	 */
	boolean loadSettings(File file);
	
	/**
	 * From View to Model
	 */
	void start();
	
	/**
	 * From View to Model
	 */
	void setOutDir(File selectedFile);
	
	/**
	 * From View to Model
	 */
	boolean setSelectedFiles(File[] inputFiles);
	
	/**
	 * From View to Model
	 */
	void detect();

}
