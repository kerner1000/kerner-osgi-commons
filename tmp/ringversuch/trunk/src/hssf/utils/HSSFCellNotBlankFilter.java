package hssf.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFCellNotBlankFilter implements HSSFCellFilter {

	@Override
	public boolean accept(HSSFCell cell) {
		return (cell.getCellType() != HSSFCell.CELL_TYPE_BLANK);
	}

}
