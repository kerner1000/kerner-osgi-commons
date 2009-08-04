package hssf.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFCellColumnFilter implements HSSFCellFilter {
	
	public enum Index {
		BELOW, ABOVE, EQUAL
	}
	private final int colIndex;
	private final Index index;
	
	public HSSFCellColumnFilter(int colIndex, Index index) {
		this.colIndex = colIndex;
		this.index = index;
	}

	@Override
	public boolean accept(HSSFCell cell) {
		final int i = cell.getColumnIndex();
		switch (index) {
		case BELOW:
			return (colIndex > i);
		case ABOVE:
			return (colIndex < i);
		case EQUAL:
			return (colIndex == i);
		default:
			return (colIndex == i);
		}
	}
}
