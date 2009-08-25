package de.fh.giessen.ringversuch.view;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>Handle incoming events (from Model bzw. Controller to View).</p>
 * @author Alexander Kerner
 *
 */
public interface ViewIn {

	/**
	 * from Model to View
	 */
	void printMessage(String message, boolean isError);

	/**
	 * from Model to View
	 */
	void setOnline();

	/**
	 * from Model to View
	 */
	void setWorking();
	
	/**
	 * from Model to View
	 */
	void setReady();

	/**
	 * from Model to View
	 */
	void showError(String message);

	/**
	 * from Model to View
	 */
	ViewTypeSettings getSettings();
	
	/**
	 * from Model to View
	 */
	void setSettings_view(ViewTypeSettings settings);

	/**
	 * from Model to View
	 */
	void setProgress(int current, int max);
	
}
