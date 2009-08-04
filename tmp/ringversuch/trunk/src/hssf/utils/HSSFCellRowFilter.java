package hssf.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFCellRowFilter implements HSSFCellFilter {

	public enum Index {
		BELOW, ABOVE, EQUAL
	}
	private final int rowIndex;
	private final Index index;
	
	public HSSFCellRowFilter(int rowIndex, Index index) {
		this.rowIndex = rowIndex;
		this.index = index;
	}

	@Override
	public boolean accept(HSSFCell cell) {
		final int i = cell.getColumnIndex();
		switch (index) {
		case BELOW:
			return (rowIndex > i);
		case ABOVE:
			return (rowIndex < i);
		case EQUAL:
			return (rowIndex == i);
		default:
			return (rowIndex == i);
		}
	}

}
