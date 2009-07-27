package de.fh.giessen.ringversuch.model;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

class ProbeIdentCellFormatter implements CellFormatter {
	
	private final HSSFCellStyle style;
	
	ProbeIdentCellFormatter(HSSFWorkbook wb, HSSFSheet sheet) {
		final HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style = wb.createCellStyle();
		style.setFont(font);
		style.setWrapText(true);
	}
	
	@Override 
	public void format(HSSFCell cell){
		cell.setCellStyle(style);
	}

}
