package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public interface SettingsManager {

	void loadSettings(File file) throws IOException, InvalidSettingsException;
	
	void saveSettings(File file) throws IOException;
	
	Properties getDefaultProperties();
	
	Properties getCurrentProperties();

	void setCurrentProperties(Properties properties) throws InvalidSettingsException;

}
