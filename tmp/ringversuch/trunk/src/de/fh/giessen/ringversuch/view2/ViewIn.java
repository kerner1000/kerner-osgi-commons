package de.fh.giessen.ringversuch.view2;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>Handle incoming events (from Model bzw. Controller to View).</p>
 * @author Alexander Kerner
 *
 */
public interface ViewIn {

	void printMessage(String message, boolean isError);

	void setOnline();

	void setWorking();
	
	void setReady();

	void showError(String message);

	ViewTypeSettings getSettings();
	
	void setSettings_view(ViewTypeSettings settings);

	void setProgress(int current, int max);
	
}
