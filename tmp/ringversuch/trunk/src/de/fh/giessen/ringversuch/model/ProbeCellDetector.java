package de.fh.giessen.ringversuch.model;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class ProbeCellDetector extends AbstractDetector {
	
	private final static Logger LOGGER = Logger.getLogger(ProbeCellDetector.class);
	private final WorkMonitor monitor;

	ProbeCellDetector(File[] files, ModelSettings settings, WorkMonitor monitor) {
		super(files, settings);
		this.monitor = monitor;
	}

	@Override
	public ModelSettings detect(ModelSettings settings, File[] files) throws Exception {
		int probeCellRow;
		int probeCellCol;
		for(File f : files){
			final HSSFCell probeCell = Core.detectProbeCell(f);
			probeCellRow = probeCell.getRowIndex();
			probeCellCol = probeCell.getColumnIndex();
			final String s = "found cell containing probe ident: " + probeCellRow + ","+ probeCellCol;
			LOGGER.info(s);
			settings.setProbeIdentRow(probeCellRow);
			settings.setProbeIdentColumn(probeCellCol);
			monitor.printMessage(s);
			// TODO considering only first file
			break;
		}
		return settings;
	}
}
