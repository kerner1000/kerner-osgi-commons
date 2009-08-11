package de.fh.giessen.ringversuch.model;

import java.io.File;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class ColumnOfSubstancesDetector extends AbstractDetector {
	
	private final static Logger LOGGER = Logger.getLogger(ColumnOfSubstancesDetector.class);
	private final WorkMonitor monitor;

	ColumnOfSubstancesDetector(File[] files, ModelSettings settings, WorkMonitor monitor) {
		super(files, settings);
		this.monitor = monitor;
	}

	@Override
	public ModelSettings detect(ModelSettings settings, File[] files)
			throws Exception {
		for(File f : files){
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
