package de.fh.giessen.ringversuch.model;

import java.io.File;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

interface Detector {

	ModelSettings detect(ModelSettings settings, File[] files) throws Exception;
	
}
