package de.fh.giessen.ringversuch.model;

import java.io.File;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

/**
 * 
 * @ThreadSave state final
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 * 
 */
class ColumnOfSubstancesDetector extends AbstractDetector {

	private final static Logger LOGGER = Logger
			.getLogger(ColumnOfSubstancesDetector.class);
	private final WorkMonitor monitor;

	ColumnOfSubstancesDetector(File[] files, ModelSettings settings,
			WorkMonitor monitor) {
		super(files, settings);
		this.monitor = monitor;
	}

	@Override
	public ModelSettings detect(ModelSettings settings, File[] files)
			throws Exception {
		// null file check already done in constructor of AbstractDectector
		for (File f : files) {
			// TODO validity check?
			final int col = Core.detectSubstancesCol(f);
			final String s = "assuming column of substances: " + col;
			LOGGER.info(s);
			settings.setSubstancesColumn(col);
			monitor.printMessage(s);
			// TODO considering only first file
			break;
		}
		return settings;
	}
}
