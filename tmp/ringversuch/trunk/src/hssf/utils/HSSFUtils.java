package hssf.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class HSSFUtils {
	
	private HSSFUtils(){}

	public static HSSFCell getNextStringBasedCellInRow(HSSFSheet sheet, final HSSFCell cellOrig) {
		HSSFCell result = null;
		final HSSFCellTypeFilter f1 = new StringBasedCellFilter();
		final HSSFCellTypeFilter f2 = new HSSFCellTypeFilter() {
			@Override
			public boolean accept(HSSFCell cell) {
				return (cell.getRowIndex() == cellOrig.getRowIndex() && cell.getColumnIndex() > cellOrig.getColumnIndex());
			}
		};
		
		AbstractHSSFSheetWalker walker = new AbstractHSSFSheetWalker(sheet) {
			@Override
			public void handleCell(HSSFCell cell) {
				result = cell;
			}
		};
		walker.addHSSFCellFilter(f1);
		walker.addHSSFCellFilter(f2);
		if(result == null)
			throw new NullPointerException("could not get cell");
		return result;
	}

}
