package hssf.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public interface HSSFCellFilter {
	
	boolean accept(HSSFCell cell);

}
