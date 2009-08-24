package de.fh.giessen.ringversuch.model;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 * 
 */
interface CellFormatter {

	void format(HSSFCell cell);

}
