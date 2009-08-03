package de.fh.giessen.ringversuch.model;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFCell;

class HSSFCellFilterOnlyStringCellValues implements HSSFCellFilter {
	@Override
	public Collection<HSSFCell> filter(Collection<HSSFCell> cells) {
		final Collection<HSSFCell> result = new ArrayList<HSSFCell>();
		for(HSSFCell c : cells){
			if(c.getCellType() == HSSFCell.CELL_TYPE_STRING && !c.getRichStringCellValue().getString().isEmpty())
				result.add(c);
		}
		return result;
	}
}
