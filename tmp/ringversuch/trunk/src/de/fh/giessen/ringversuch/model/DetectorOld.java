package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class DetectorOld implements Callable<ModelSettings> {

	private final File[] inFiles;
	private final WorkMonitor monitor;
	private final ModelSettings settings;
	private final static Logger LOGGER = Logger.getLogger(DetectorOld.class);
	
	DetectorOld(File[] inputFiles, WorkMonitor monitor, ModelSettings settings) {
		this.inFiles = inputFiles;
		this.monitor = monitor;
		this.settings = settings;
	}

	@Override
	public ModelSettings call() throws Exception {
		if(inFiles == null || inFiles.length == 0)
			throw new NullPointerException("files not selected yet");
		int probeCellRow;
		int probeCellCol;
		int laborCellRow;
		int laborCellCol;
		int valuesBeginCellRow;
		int valuesBeginCellCol;
		int valuesEndCellRow;
		int valuesEndCellCol;
		int substancesCol;
		for(File f : inFiles){
			final HSSFCell probeCell = Core.detectProbeCell(f);
			probeCellRow = probeCell.getRowIndex();
			probeCellCol = probeCell.getColumnIndex();
			final String s = "got cell containing probe ident: " + probeCellRow + ","+ probeCellCol;
			LOGGER.info(s);
			monitor.printMessage(s);
			settings.setProbeIdentRow(probeCellRow);
			settings.setProbeIdentColumn(probeCellCol);
			
			
			final HSSFCell laborCell = Core.detectLaborCell(f);
			laborCellRow = laborCell.getRowIndex();
			laborCellCol = laborCell.getColumnIndex();
			final String s2 = "got cell containing labor ident: " + laborCellRow + ","+ laborCellCol;
			LOGGER.info(s2);
			monitor.printMessage(s2);
			settings.setLaborIdentRow(laborCellRow);
			settings.setLaborIdentColumn(laborCellCol);
			
			final HSSFCell valuesBeginCell = Core.detectValuesBeginCell(f);
			valuesBeginCellRow = valuesBeginCell.getRowIndex();
			valuesBeginCellCol = valuesBeginCell.getColumnIndex();
			final String s4 = "got cell containing first value: " + valuesBeginCellRow + ","+ valuesBeginCellCol;
			LOGGER.info(s4);
			monitor.printMessage(s4);
			settings.setValuesStartColumn(valuesBeginCellCol);
			settings.setValuesStartRow(valuesBeginCellRow);
			
			final HSSFCell valuesEndCell = Core.detectValuesEndCell(f);
			valuesEndCellRow = valuesEndCell.getRowIndex();
			valuesEndCellCol = valuesEndCell.getColumnIndex();
			final String s5 = "got cell containing last value: " + valuesEndCellRow + ","+ valuesEndCellCol;
			LOGGER.info(s5);
			monitor.printMessage(s5);
			settings.setValuesEndColumn(valuesEndCellCol);
			settings.setValuesEndRow(valuesEndCellRow);
			
			substancesCol = Core.detectSubstancesCol(f);
			final String s3 = "got column containing substances: " + substancesCol;
			LOGGER.info(s3);
			monitor.printMessage(s3);

			// TODO considering only first file
			break;
		}
		return settings;
	}

}
