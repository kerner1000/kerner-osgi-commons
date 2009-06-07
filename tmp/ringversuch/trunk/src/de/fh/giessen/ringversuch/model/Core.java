package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

class Core {
	
	private Core(){}

	private final static char REPLACEMENT = '_';
	private final static Logger LOGGER = Logger.getLogger(Core.class);

	static Labor readLaborFile(final File file, final SettingsManager settings) throws FileNotFoundException, IOException{
		final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
	    final HSSFWorkbook wb = new HSSFWorkbook(fs);
	    final int no = wb.getNumberOfSheets();
	    for(int i=0;i<no;i++){
	    	final HSSFSheet sheet = wb.getSheetAt(i);
	    	System.out.println(getProbeFromSheet(sheet, settings));
	    }
	    System.out.println("--");
	    
	    return null;
	}
	
	private static Probe getProbeFromSheet(final HSSFSheet sheet, final SettingsManager settings){
		final int rowIndex = settings.getProbeRowIndex();
		final HSSFRow row = sheet.getRow(rowIndex);
		final HSSFCell cell = row.getCell(settings.getProbeCellNum());
		final String probe = cell.toString();
		final String labor = getLaborFromSheet(sheet, settings);
		System.out.println("ProbeNo Zelle: " + probe);
		System.out.println("LaborNo Zelle: " + labor);
		final Collection<Analyse> analyses = getAnalysesFromSheet(sheet, settings);
		return null;
	}

	private static Collection<Analyse> getAnalysesFromSheet(
			final HSSFSheet sheet, final SettingsManager settings) {
		final int startRow = settings.getValuesStartRow();
		final int startColumn = settings.getValuesStartColumn();
		final int endRow = settings.getValuesEndRow();
		final int endColumn = settings.getValuesEndColumn();
		final Collection<Analyse> analyses = new ArrayList<Analyse>();
		HSSFRow currentRow = null;
		HSSFCell currentCell = null;
		for(int i = startRow; i<endRow; i++){
			for(int j = startColumn; j<endColumn;j++){
				// select cell at i, j
				currentRow = sheet.getRow(i);
				currentCell = currentRow.getCell(j);
				System.out.print(currentCell.toString() + ", ");
			}
			System.out.println();
		}
		return null;
	}

	private static String getLaborFromSheet(final HSSFSheet sheet,
			final SettingsManager settings) {
		final int rowIndex = settings.getLaborRowIndex();
		final HSSFRow row = sheet.getRow(rowIndex);
		final HSSFCell cell = row.getCell(settings.getLaborCellNum());
		return cell.toString();
	}

	/**
	 * <p>
	 * This method will take a string and replace all characters with {@code
	 * REPLACEMENT}, that can cause any problems on actions related to file
	 * system. <br>
	 * These problematical characters are for instance OS dependent, illegal
	 * filename characters or filename separators.
	 * </p>
	 * Attention: Directory separators such as "/" will be removed! <br>
	 * Don't use strings, that represent pathnames.
	 * 
	 * @param string
	 *            String, that is to be reformatted.
	 * @return the new String, containing only chars, that are not
	 *         problematically
	 */
	static String formatToValidString(String string) {
		char[] chars = string.toCharArray();
		StringBuilder sb = new StringBuilder();
		Pattern p = Pattern.compile("[a-zA-Z0-9_-]");
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
