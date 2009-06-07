package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import Exceptions.ProbeNoNotFoundException;

class Core {
	
	private Core(){}

	private final static char REPLACEMENT = '_';

	static Labor readLaborFile(final File file, final SettingsManagerImpl settings) throws FileNotFoundException, IOException{
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
	
	private static Probe getProbeFromSheet(final HSSFSheet sheet, final SettingsManagerImpl settings){
		final int rowIndex = settings.getProbeRowIndex();
		final HSSFRow row = sheet.getRow(rowIndex);
		HSSFCell cell = row.getCell(cellnum);
		String probe = cell.toString();
		
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
