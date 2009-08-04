package hssf.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFCellTypeNumericFilter implements HSSFCellFilter {
	@Override
	public boolean accept(HSSFCell cell) {
		return (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC);
	}
}
