package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.concurrent.Callable;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

/**
 * 
 * @ThreadSave state final
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 * 
 */
abstract class AbstractDetector implements Detector, Callable<ModelSettings> {

	private final File[] files;
	private final ModelSettings settings;

	AbstractDetector(File[] files, ModelSettings settings) {
		if (files == null || files.length == 0)
			throw new NullPointerException("files not selected yet");
		this.files = files;
		this.settings = settings;
	}

	@Override
	public ModelSettings call() throws Exception {
		return detect(settings, files);
	}

}
