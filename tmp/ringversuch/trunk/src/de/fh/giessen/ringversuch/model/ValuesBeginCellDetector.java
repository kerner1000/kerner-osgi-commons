package de.fh.giessen.ringversuch.model;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class ValuesBeginCellDetector extends AbstractDetector {
	
	private final static Logger LOGGER = Logger.getLogger(ValuesBeginCellDetector.class);
	private final WorkMonitor monitor;

	ValuesBeginCellDetector(File[] files, ModelSettings settings, WorkMonitor monitor) {
		super(files, settings);
		this.monitor = monitor;
	}

	@Override
	public ModelSettings detect(ModelSettings settings, File[] files)
			throws Exception {
		int cellRow;
		int cellCol;
		for(File f : files){
			final HSSFCell cell = Core.detectValuesBeginCell(f);
			cellRow = cell.getRowIndex();
			cellCol = cell.getColumnIndex();
			final String s = "got cell containing first value: " + cellRow + ","+ cellCol;
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
