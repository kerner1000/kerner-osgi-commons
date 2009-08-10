package hssf.utils;

import java.util.Comparator;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFCellColumnComparator implements Comparator<HSSFCell> {

	@Override
	public int compare(HSSFCell o1, HSSFCell o2) {
		final int c1 = o1.getColumnIndex();
		final int c2 = o2.getColumnIndex();
		return c1 - c2;
	}

}
