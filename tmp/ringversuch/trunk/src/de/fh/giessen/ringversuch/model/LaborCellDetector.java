package de.fh.giessen.ringversuch.model;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class LaborCellDetector extends AbstractDetector {

	private final static Logger LOGGER = Logger.getLogger(LaborCellDetector.class);
	private final WorkMonitor monitor;
	
	LaborCellDetector(File[] files, ModelSettings settings, WorkMonitor monitor) {
		super(files, settings);
		this.monitor = monitor;
	}

	@Override
	public ModelSettings detect(ModelSettings settings, File[] files)
			throws Exception {
		int cellRow;
		int cellCol;
		for(File f : files){
			final HSSFCell cell = Core.detectLaborCell(f);
			cellRow = cell.getRowIndex();
			cellCol = cell.getColumnIndex();
			final String s = "found cell containing labor ident: " + cellRow + ","+ cellCol;
			LOGGER.info(s);
			settings.setLaborIdentRow(cellRow);
			settings.setLaborIdentColumn(cellCol);
			monitor.printMessage(s);
			// TODO considering only first file
			break;
		}
		return settings;
	}
}
