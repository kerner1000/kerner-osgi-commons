package de.fh.giessen.ringversuch.model;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class ValuesEndCellDetector extends AbstractDetector {
	
	private final static Logger LOGGER = Logger.getLogger(ValuesEndCellDetector.class);
	private final WorkMonitor monitor;

	ValuesEndCellDetector(File[] files, ModelSettings settings, WorkMonitor monitor) {
		super(files, settings);
		this.monitor = monitor;
	}

	@Override
	public ModelSettings detect(ModelSettings settings, File[] files)
			throws Exception {
		int cellRow;
		int cellCol;
		for(File f : files){
			final HSSFCell cell = Core.detectValuesEndCell(f);
			cellRow = cell.getRowIndex();
			cellCol = cell.getColumnIndex();
			final String s = "got cell containing last value: " + cellRow + ","+ cellCol;
			LOGGER.info(s);
			settings.setValuesStartColumn(cellCol);
			settings.setValuesStartRow(cellRow);
			monitor.printMessage(s);
			// TODO considering only first file
			break;
		}
		return settings;
	}

}
