package de.fh.giessen.ringversuch.model;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFCell;

class HSSFCellFilterOnlyNumericCellValues implements HSSFCellFilter {
	@Override
	public Collection<HSSFCell> filter(Collection<HSSFCell> cells) {
		final Collection<HSSFCell> result = new ArrayList<HSSFCell>();
		for(HSSFCell c : cells){
			if(c.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
				result.add(c);
		}
		return result;
	}
}
