package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;


public interface Model {

	void setOutDir(File selectedDir);

	void setSelectedFiles(File[] inputFiles);

	boolean start() throws CancellationException, InterruptedException, ExecutionException;

	ModelSettings getSettings();
	
	void setSettings(ModelSettings settings);
	
	void cancel();

}
