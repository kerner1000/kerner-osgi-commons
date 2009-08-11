package de.fh.giessen.ringversuch.model;

import java.io.File;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

public interface Model {

	void setOutDir(File selectedDir);

	void setSelectedFiles(File[] inputFiles);

	void start() throws Exception;

	ModelSettings getSettings();

	void setSettings(ModelSettings settings);

	void cancel();

	void detectProbeCell() throws Exception;

	void detectLaborCell() throws Exception;

	void detectValuesBeginCell() throws Exception;

	void detectValuesEndCell() throws Exception;

	void detectColumnOfSubstances() throws Exception;

}
