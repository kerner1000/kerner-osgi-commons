package de.fh.giessen.ringversuch.view;

import de.fh.giessen.ringversuch.view.settings.ViewSettings;

public interface View {
	
	void printMessage(String message, boolean isError);

	void setOnline();

	void setWorking();
	
	void setReady();

	void showError(String message);
	
	ViewSettings getSettings();
	
	void setSettings(ViewSettings settings);

	void setProgress(int current, int max);

	

	
}
