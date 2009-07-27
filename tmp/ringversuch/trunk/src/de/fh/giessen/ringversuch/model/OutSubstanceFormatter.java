package de.fh.giessen.ringversuch.model;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

interface OutSubstanceFormatter {
	
	void format();
	
	void formatProbeIdentCell(HSSFCell cell);

	void formatSheet(HSSFSheet sheet);

	void formatHeaderLine(Iterator<HSSFCell> cellIterator);

}
