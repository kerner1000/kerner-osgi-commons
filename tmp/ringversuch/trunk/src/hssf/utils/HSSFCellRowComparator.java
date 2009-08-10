package hssf.utils;

import java.util.Comparator;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFCellRowComparator implements Comparator<HSSFCell>{

	@Override
	public int compare(HSSFCell o1, HSSFCell o2) {
		final int r1 = o1.getRowIndex();
		final int r2 = o2.getRowIndex();
		return r1 - r2;
	}

}
