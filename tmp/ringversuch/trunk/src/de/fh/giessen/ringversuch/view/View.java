package de.fh.giessen.ringversuch.view;

public interface View {
	
	void printMessage(String message, boolean isError);

	void setOnline();

	void setWorking();
	
	void setReady();

	void showError(String message);
	
	SettingsView getSettings();
	
	void setSettings(SettingsView settings);

	void setProgress(int current, int max);

	

	
}
