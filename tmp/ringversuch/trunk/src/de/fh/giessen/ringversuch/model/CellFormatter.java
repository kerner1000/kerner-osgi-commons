package de.fh.giessen.ringversuch.model;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * not public, so no doc
 * 
 * @author Alexander Kerner
 * 
 */
interface CellFormatter {

	void format(HSSFCell cell);

}
