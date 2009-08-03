package hssf.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public interface HSSFCellTypeFilter {
	
	boolean accept(HSSFCell cell);

}
