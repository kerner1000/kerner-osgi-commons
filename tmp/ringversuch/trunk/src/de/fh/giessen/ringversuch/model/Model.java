package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface Model {

	void setOutDir(File selectedDir);

	void setSelectedFiles(File[] inputFiles);
	
	void loadSettings(File settingsFile) throws IOException, InvalidSettingsException;

	void start();

	
}
