package de.fh.giessen.ringversuch.model;

import java.io.File;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;


public interface Model {

	void setOutDir(File selectedDir);

	void setSelectedFiles(File[] inputFiles);

	// TODO should be void
	boolean start() throws Exception;

	ModelSettings getSettings();
	
	void setSettings(ModelSettings settings);
	
	void cancel();

	void detect() throws Exception;

}
