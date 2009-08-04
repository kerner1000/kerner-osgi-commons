package hssf.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFCellTypeStringFilter implements HSSFCellFilter {

	@Override
	public boolean accept(HSSFCell cell) {
		return (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING
				&& cell.getRichStringCellValue() != null && !cell
				.getRichStringCellValue().toString().isEmpty());
	}

}
