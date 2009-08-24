package de.fh.giessen.ringversuch.model;

import java.io.File;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

/**
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 *
 */
interface Detector {

	ModelSettings detect(ModelSettings settings, File[] files) throws Exception;
	
}
