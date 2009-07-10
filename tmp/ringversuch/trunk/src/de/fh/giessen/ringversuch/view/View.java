package de.fh.giessen.ringversuch.view;

import java.util.Properties;


public interface View {
	
	void printMessage(String message, boolean isError);

	void setOnline();

	void setWorking();

	void showError(String message);
	
	void setSettingsOut(Properties settings);
	
	boolean setSettingsIn(Properties settings);
	
}
