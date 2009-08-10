package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.concurrent.Callable;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

abstract class AbstractDetector implements Detector, Callable<ModelSettings>{
	
	private final File[] files;
	private final ModelSettings settings;
	
	AbstractDetector(File[] files, ModelSettings settings) {
		this.files = files;
		this.settings = settings;
	}
	
	@Override
	public ModelSettings call() throws Exception {
		if(files == null || files.length == 0)
			throw new NullPointerException("files not selected yet");
		return detect(settings, files);
	}

}
