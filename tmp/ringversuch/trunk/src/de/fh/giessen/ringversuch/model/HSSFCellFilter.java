package de.fh.giessen.ringversuch.model;

import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFCell;

interface HSSFCellFilter {
	
	Collection<HSSFCell> filter(Collection<HSSFCell> cells);
	
}
