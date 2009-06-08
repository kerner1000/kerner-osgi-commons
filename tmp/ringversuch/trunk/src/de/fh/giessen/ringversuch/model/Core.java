package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

class Core {

	private Core() {
	}

	private final static char REPLACEMENT = '_';
	private final static Logger LOGGER = Logger.getLogger(Core.class);
	public static final String NEW_LINE = System.getProperty("line.separator");

	static Labor readLaborFile(final File file, final SettingsManager settings)
			throws FileNotFoundException, IOException, InvalidFormatException {
		LOGGER.debug("reading file " + file);
		final POIFSFileSystem fs = new POIFSFileSystem(
				new FileInputStream(file));
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final int no = wb.getNumberOfSheets();
		LOGGER.debug("no of sheets: " + no);
		final Collection<Probe> probes = new ArrayList<Probe>();
		final String laborIdent = getLaborIdentFromSheet(wb.getSheetAt(0),
				settings);
		for (int i = 0; i < no; i++) {
			final HSSFSheet sheet = wb.getSheetAt(i);
			final Probe probe = getProbeFromSheet(sheet, settings);
			LOGGER.debug("got probe from sheet " + (i + 1) + ": " + probe);
			probes.add(probe);
		}
		final Labor labor = new LaborImpl(laborIdent, probes);
		LOGGER.debug("done for file " + file);
		return labor;
	}

	private static Probe getProbeFromSheet(final HSSFSheet sheet,
			final SettingsManager settings) {
		final int rowIndex = settings.getProbeRowIndex();
		final int columnIndex = settings.getProbeColumnIndex();
		LOGGER.debug("probe indices: " + rowIndex + " " + columnIndex);
		final HSSFRow row = sheet.getRow(rowIndex);
		final HSSFCell cell = row.getCell(columnIndex);
		final String probe = cell.toString();
		LOGGER.debug("probe ident: " + probe);
		final String labor = getLaborIdentFromSheet(sheet, settings);
		LOGGER.debug("labor ident: " + labor);
		final Collection<Analyse> analyses = getAnalysesFromSheet(sheet,
				settings);
		return new ProbeImpl(labor, probe, analyses);
	}

	private static Collection<Analyse> getAnalysesFromSheet(
			final HSSFSheet sheet, final SettingsManager settings) {
		final int startRow = settings.getValuesStartRowIndex();
		final int startColumn = settings.getValuesStartColumnIndex();
		final int endRow = settings.getValuesEndRowIndex();
		final int endColumn = settings.getValuesEndColumnIndex();
		LOGGER.debug("got values start indices: " + startRow + " "
				+ startColumn);
		LOGGER.debug("got values end indices: " + endRow + " " + endColumn);
		final Collection<Analyse> analyses = new ArrayList<Analyse>();
		HSSFRow currentRow = null;
		HSSFCell currentCell = null;
		String currentSubstance = null;
		final int substancesColumnIndex = settings.getSubstancesColumnIndex();
		LOGGER.debug("substances at column: " + substancesColumnIndex);
		for (int i = startColumn; i <= endColumn; i++) {
			final String analyseIdent = Integer.toString(i - startColumn + 1);
			final Collection<Substance> substances = new ArrayList<Substance>();
			for (int j = startRow; j <= endRow; j++) {
				currentRow = sheet.getRow(j);
				currentCell = currentRow.getCell(i);
				currentSubstance = currentRow.getCell(substancesColumnIndex)
						.toString();
				currentSubstance = formatToValidString(currentSubstance);
				final Substance substance = new SubstanceImpl(currentSubstance,
						currentCell.toString());
				LOGGER.debug("cell " + j + " " + i + " analyse " + analyseIdent
						+ ", substance " + substance);
				substances.add(substance);
			}
			analyses.add(new AnalyseImpl(analyseIdent, substances));
		}
		return analyses;
	}

	private static String getLaborIdentFromSheet(final HSSFSheet sheet,
			final SettingsManager settings) {
		final int rowIndex = settings.getLaborRowIndex();
		final HSSFRow row = sheet.getRow(rowIndex);
		final HSSFCell cell = row.getCell(settings.getLaborColumnIndex());
		return cell.toString();
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

	public static Collection<OutSubstance> getOutSubstancesFromLabors(
			final Collection<Labor> labors, final SettingsManager settings)
			throws InvalidFormatException {
		final Collection<OutSubstance> result = new ArrayList<OutSubstance>();
		final String probeIdent = settings.getProbeIdent();
		Collection<String> commonKeys = getCommonSubstanceKeys(labors,
				probeIdent);
		for (String s : commonKeys) {
			result.add(new OutSubstanceImpl(s, probeIdent,
					getOutSubstanceEntry(s, labors, probeIdent)));
		}
		return result;
	}

	private static Collection<OutSubstanceEntry> getOutSubstanceEntry(
			final String substanceIdent, final Collection<Labor> labors,
			final String probeIdent) {
		final Collection<OutSubstanceEntry> result = new ArrayList<OutSubstanceEntry>();
		for (Labor l : labors) {
			Probe p = l.getProbe(probeIdent);
			Collection<String> values = new ArrayList<String>();
			for (Analyse a : p.getAnalyses()) {
				values.add(a.getValueForSubstance(substanceIdent));
			}
			result.add(new OutSubstanceEntryImpl(l.getIdentifier(), values));
		}
		return result;
	}

	public static Collection<String> getCommonSubstanceKeys(
			final Collection<Labor> labors, final String probeIdent)
			throws InvalidFormatException {
		Collection<String> keys = null;
		for (Labor l : labors) {
			final Probe p = l.getProbe(probeIdent);
			Collection<String> tmpKeys = p.getCommonSubstanceKeys();
			if (keys == null) {
				keys = new ArrayList<String>(tmpKeys);
			} else {
				if (!Core.collectionsAreEqual(keys, tmpKeys)) {
					InvalidFormatException e = new InvalidFormatException(
							"inconsistent list of substances detected. Labor:"
									+ l.getIdentifier() + ", Probe:"
									+ p.getIdentifier());
					LOGGER.error(
							"inconsistent list of substances detected. Labor:"
									+ l.getIdentifier() + ", Probe:"
									+ p.getIdentifier(), e);
					throw e;
				}
			}
		}
		return keys;
	}

	public static boolean collectionsAreEqual(final Collection<String> col1,
			final Collection<String> col2) {
		for (String s : col1) {
			if (!col2.contains(s)) {
				LOGGER.error("collections not equal:\n" + col1 + "\n" + col2);
				return false;
			}
		}
		return true;
	}

	public static String getFileNameForOutSubstance(final OutSubstance s) {
		final StringBuilder sb = new StringBuilder();
		sb.append(s.getProbeIdent());
		sb.append("_");
		sb.append(s.getIdent());
		return sb.toString();
	}

	public static void writeOutSubstance(final OutSubstance s, final File file) throws IOException {
		LOGGER.debug("writing file " + file);
		final HSSFWorkbook wb = new HSSFWorkbook();
		final HSSFSheet sheet = wb.createSheet();
		
		int currentRow = 5;
		for(OutSubstanceEntry entry : s.getEntries()){
			int currentColumn = 3;
			for(String value : entry.getValues()){
				LOGGER.debug("writing " + value + " to " + currentRow + " " + currentColumn);
				final HSSFRow valueRow = sheet.createRow(currentRow);
				final HSSFCell valueCell = valueRow.createCell(currentColumn);
				valueCell.setCellValue(new HSSFRichTextString(value));
				currentColumn++;
			}
			currentRow++;
		}
		
		FileOutputStream fs = new FileOutputStream(file);
		wb.write(fs);
		fs.close();

	}

	
}
