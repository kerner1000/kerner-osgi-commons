package de.fh.giessen.ringversuch.model;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

class OutSubstanceFormatterImpl implements OutSubstanceFormatter  {

	private final HSSFWorkbook wb;
	private final HSSFSheet sheet;

	OutSubstanceFormatterImpl(HSSFWorkbook wb, HSSFSheet sheet) {
		this.wb = wb;
		this.sheet = sheet;
	}

	@Override
	public void format() {
		
		final HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 194, // RGB
				// red
				(byte) 214, // RGB green
				(byte) 154 // RGB blue
				);
		// Fontdaten Stil usw. Objekte erstellen
		// Fettschrift mit gruenem Hintergrund und fetter Umrandung
		final HSSFFont fnt1 = wb.createFont();
		fnt1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		final HSSFCellStyle bo = wb.createCellStyle();
		bo.setFont(fnt1);
		bo.setFillForegroundColor(HSSFColor.GREEN.index);
		bo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		bo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		bo.setBottomBorderColor(HSSFColor.BLACK.index);
		bo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		bo.setLeftBorderColor(HSSFColor.BLACK.index);
		bo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		bo.setRightBorderColor(HSSFColor.BLACK.index);
		bo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		bo.setTopBorderColor(HSSFColor.BLACK.index);
		bo.setWrapText(true);

		// Normalschrift mit gelbem Hintergrund und fetter Umrandung
		final HSSFFont fnt = wb.createFont();
		fnt.setColor(HSSFFont.COLOR_NORMAL);
		final HSSFCellStyle cs = wb.createCellStyle();
		cs.setFont(fnt);
		cs.setWrapText(true);

		// Fettschrift
		final HSSFFont fnt2 = wb.createFont();
		fnt2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		final HSSFCellStyle fs = wb.createCellStyle();
		fs.setFont(fnt2);
		fs.setWrapText(true);
	
	}

	@Override
	public void formatProbeIdentCell(HSSFCell cell) {
		new ProbeIdentCellFormatter(wb, sheet).format(cell);
	}

	@Override
	public void formatSheet(HSSFSheet sheet) {
		sheet.setColumnWidth(0, 5500);
		sheet.setColumnWidth(2, 3000);
	}

	@Override
	public void formatHeaderLine(Iterator<HSSFCell> cellIterator) {
		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 194, // RGB
				// red
				(byte) 214, // RGB green
				(byte) 154 // RGB blue
				);
		final HSSFFont fnt1 = wb.createFont();
		fnt1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		final HSSFCellStyle bo = wb.createCellStyle();
		bo.setFont(fnt1);
		bo.setFillForegroundColor(HSSFColor.GREEN.index);
		bo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		bo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		bo.setBottomBorderColor(HSSFColor.BLACK.index);
		bo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		bo.setLeftBorderColor(HSSFColor.BLACK.index);
		bo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		bo.setRightBorderColor(HSSFColor.BLACK.index);
		bo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		bo.setTopBorderColor(HSSFColor.BLACK.index);
		bo.setWrapText(true);
		while(cellIterator.hasNext()){
			final HSSFCell c = cellIterator.next();
			c.setCellStyle(bo);
		}
	}

}
