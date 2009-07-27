package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

class OutSubstanceWriter {

	private final static int PROBE_IDENT_ROW = 2;
	private final static int PROBE_IDENT_COL = 0;
	private final static Logger LOGGER = Logger
			.getLogger(OutSubstanceWriter.class);
	private final static char REPLACEMENT = '_';
	private final static String PROBE_IDENT_PREFIX = "Probe Nr.: ";
	private final static String PROBE_IDENT_POSTFIX = "";
	private final static int HEADER_ROW = PROBE_IDENT_ROW + 2;
	private final static String SUBSTANZ = "Substanz";
	private final static String LNAME = "Lname";
	private final OutSubstance s;
	private final File outDir;
	private final HSSFWorkbook wb = new HSSFWorkbook();
	private final HSSFSheet sheet = wb.createSheet();

	OutSubstanceWriter(OutSubstance s, File outDir) {
		this.s = s;
		this.outDir = outDir;
	}

	void write() throws Exception {
		final String fileName = getFileName();
		LOGGER.debug("generating file " + fileName);
		writeProbeNr();
		writeHeaderRow();
		writeValues();
		format();
		final FileOutputStream fs = new FileOutputStream(new File(outDir, fileName + ".xls"));
		LOGGER.debug("writing file " + fileName);
		wb.write(fs);
		fs.flush();
		fs.close();
	}
	
	private void format() {
		OutSubstanceFormatter formatter = new OutSubstanceFormatterImpl(wb, sheet);
		HSSFRow row = sheet.getRow(PROBE_IDENT_ROW);
		HSSFCell cell = row.getCell(PROBE_IDENT_COL);
		formatter.formatSheet(sheet);
		HSSFRow row2 = sheet.getRow(HEADER_ROW);
		formatter.formatHeaderLine(row2.cellIterator());
		formatter.formatProbeIdentCell(cell);
	}

	public File getOutFile() {
		return new File(outDir, getFileName() + ".xls");
	}

	private String getFileName() {
		final StringBuilder sb = new StringBuilder();
		sb.append(s.getProbeIdent());
		sb.append("_");
		sb.append(s.getSubstanceIdent());
		return formatToValidString(sb.toString());
	}

	private void writeProbeNr() {
		final HSSFRow row = sheet.createRow(PROBE_IDENT_ROW);
		final HSSFCell cell = row.createCell(PROBE_IDENT_COL);
		cell.setCellValue(new HSSFRichTextString(new StringBuilder().append(
				PROBE_IDENT_PREFIX).append(s.getProbeIdent()).append(
				PROBE_IDENT_POSTFIX).toString()));
	}

	private void writeHeaderRow() {
		final HSSFRow row = sheet.createRow(HEADER_ROW);
		final HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString(SUBSTANZ));
		final HSSFCell cell2 = row.createCell(1);
		cell2.setCellValue(new HSSFRichTextString(LNAME));
		final HSSFCell cell3 = row.createCell(2);
		cell3.setCellValue(new HSSFRichTextString("info"));
		// TODO suboptimal
		for (int i = 3; i < 3 + s.getEntries().iterator().next().getValues().size(); i++) {
			final HSSFCell cell4 = row.createCell(i);
			cell4.setCellValue(new HSSFRichTextString(Integer.toString(i - 2)));
		}
	}

	private void writeValues() {
		int currentRow = 5;
		for (OutSubstanceEntry entry : s.getEntries()) {
			final HSSFRow valueRow = sheet.createRow(currentRow);
			int currentColumn = 3;
			for (String value : entry.getValues()) {
				final HSSFCell substanzCell = valueRow.createCell(0);
				substanzCell.setCellValue(new HSSFRichTextString(s.getSubstanceIdent()));
				final HSSFCell laborNameCell = valueRow.createCell(1);
				laborNameCell.setCellValue(new HSSFRichTextString(entry
						.getLabor().getIdentifier()));
//				LOGGER.debug("writing value=" + value + " to row=" + currentRow
//						+ ", col=" + currentColumn);
				final HSSFCell valueCell = valueRow.createCell(currentColumn);
				try{
					final double d = Double.parseDouble(value);
					valueCell.setCellValue(d);
				}catch(NumberFormatException e){
					LOGGER.error(e.getLocalizedMessage() + ", using string representation");
					valueCell.setCellValue(new HSSFRichTextString(value));
				}
				currentColumn++;
			}
			currentRow++;
		}
	}

	private static String formatToValidString(final String string) {
		final char[] chars = string.toCharArray();
		final StringBuilder sb = new StringBuilder();
		final Pattern p = Pattern.compile("[a-zA-Z0-9_-]");
		Matcher m;
		for (char c : chars) {
			m = p.matcher(new String(Character.toString(c)));
			if (m.matches()) {
				sb.append(c);
			} else {
				sb.append(REPLACEMENT);
			}
		}
		return sb.toString();
	}
}
