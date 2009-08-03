package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class Core {

	// TODO output only filenames while writing
	private Core() {
	}

	private final static Logger LOGGER = Logger.getLogger(Core.class);
	public static final String NEW_LINE = System.getProperty("line.separator");

	static HSSFWorkbook getWorkbookFromFile(File file)
			throws FileNotFoundException, IOException {
		final POIFSFileSystem fs = new POIFSFileSystem(
				new FileInputStream(file));
		return new HSSFWorkbook(fs);
	}

	static Labor readLaborFile(final File file, final ModelSettings settings)
			throws FileNotFoundException, IOException, InvalidFormatException {
		LOGGER.debug("reading file " + file);
		final HSSFWorkbook wb = getWorkbookFromFile(file);
		final int no = wb.getNumberOfSheets();
		LOGGER.debug("no of sheets: " + no);
		final Collection<Probe> probes = new ArrayList<Probe>();
		final String laborIdent = getLaborIdentFromSheet(wb.getSheetAt(0),
				settings);
		LOGGER.debug("laborIdent=" + laborIdent);
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
			final ModelSettings settings) {
		final int rowIndex = settings.getProbeIdentRow();
		final int columnIndex = settings.getProbeIdentColumn();
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
			final HSSFSheet sheet, final ModelSettings settings) {
		final int startRow = settings.getValuesStartRow();
		final int startColumn = settings.getValuesStartColumn();
		final int endRow = settings.getValuesEndRow();
		final int endColumn = settings.getValuesEndColumn();
		LOGGER.debug("got values start indices: " + startRow + " "
				+ startColumn);
		LOGGER.debug("got values end indices: " + endRow + " " + endColumn);
		final Collection<Analyse> analyses = new ArrayList<Analyse>();
		HSSFRow currentRow = null;
		HSSFCell currentCell = null;
		String currentSubstance = null;
		final int substancesColumnIndex = settings.getSubstancesColumn();
		LOGGER.debug("substances at column: " + substancesColumnIndex);
		for (int i = startColumn; i <= endColumn; i++) {
			final String analyseIdent = Integer.toString(i - startColumn + 1);
			final Collection<Substance> substances = new ArrayList<Substance>();
			for (int j = startRow; j <= endRow; j++) {
				currentRow = sheet.getRow(j);
				currentCell = currentRow.getCell(i);
				currentSubstance = currentRow.getCell(substancesColumnIndex)
						.toString();
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
			final ModelSettings settings) {
		final int rowIndex = settings.getLaborIdentRow();
		final HSSFRow row = sheet.getRow(rowIndex);
		final HSSFCell cell = row.getCell(settings.getLaborIdentColumn());
		return cell.toString();
	}

	public static Collection<OutSubstance> getOutSubstancesFromLabors(
			final Collection<Labor> labors, final ModelSettings settings)
			throws InvalidFormatException {
		final Collection<OutSubstance> result = new ArrayList<OutSubstance>();
		final String probeIdent = settings.getProbeIdent();
		Collection<String> commonKeys = getCommonSubstanceKeys(labors,
				probeIdent);
		for (String s : commonKeys) {
			result.add(new OutSubstanceImpl(s, probeIdent,
					getOutSubstanceEntrys(s, labors, probeIdent)));
		}
		return result;
	}

	public static Collection<String> getCommonSubstanceKeys(
			final Collection<Labor> labors, final String probeIdent)
			throws InvalidFormatException {
		LOGGER.debug("labors=" + labors + Preferences.NEW_LINE + "probeIdent="
				+ probeIdent);
		Collection<String> keys = null;
		for (Labor l : labors) {
			LOGGER.debug("currentLabor=" + l);
			LOGGER.debug("currentProbeIdent=" + probeIdent);
			final Probe p = l.getProbe(probeIdent);
			LOGGER.debug("got probe form labor: " + p);
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

	private static Collection<OutSubstanceEntry> getOutSubstanceEntrys(
			final String substanceIdent, final Collection<Labor> labors,
			final String probeIdent) {
		final Collection<OutSubstanceEntry> result = new ArrayList<OutSubstanceEntry>();
		for (Labor l : labors) {
			Probe p = l.getProbe(probeIdent);
			Collection<String> values = new ArrayList<String>();
			for (Analyse a : p.getAnalyses()) {
				values.add(a.getValueForSubstance(substanceIdent));
			}
			System.err.println(l);
			result.add(new OutSubstanceEntryImpl(l, values));
		}
		return result;
	}

	public static boolean collectionsAreEqual(final Collection<String> col1,
			final Collection<String> col2) {
		// TODO also check second collection
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
		sb.append(s.getSubstanceIdent());
		return sb.toString();
	}
	
	public static HSSFCell detectValuesBeginCell(File file) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static HSSFCell detectValuesEndCell(File file) {
		HSSFCell result;
		int currentMaxNums = 0;
		final Map<HSSFCell, Integer> map = new HashMap<HSSFCell, Integer>();
		LOGGER.debug("detecting cell containing first value");
		
		final HSSFWorkbook wb = getWorkbookFromFile(file);
		// TODO for now, we only look at sheet 0
		final HSSFSheet sheet = wb.getSheetAt(0);
		final Iterator<HSSFRow> i = sheet.rowIterator();
		while (i.hasNext()) {
			final HSSFRow r = (HSSFRow) i.next();
			final Iterator<HSSFCell> i2 = r.cellIterator();
			while (i2.hasNext()) {
				final HSSFCell c = (HSSFCell) i2.next();
				if(map.containsKey(c)){
					LOGGER.error("IllegalStateException", new IllegalStateException("cannot be"));
				} else {
					final HSSFCellFilter filter = new HSSFCellFilterOnlyNumericCellValues();
					final int numOfCellsAbove = filter.filter(getCellsAboveCell(sheet, c)).size();
					LOGGER.debug("cell " + c.getRowIndex() + "," + c.getColumnIndex() + " has " + numOfCellsAbove + " cells above (numeric)");
					map.put(c, numOfCellsAbove);
				}
			}
		}	
		for(Entry<HSSFCell, Integer> e : map.entrySet()){
			final HSSFCell xx = e.getKey();
			final int num = e.getValue();
			if(num > currentMaxNums){
				currentMaxNums = num;
				result = xx;
			}
		}
		return result;
	}
	
	public static int detectSubstancesCol(File file) throws FileNotFoundException, IOException {
		int result = -1;
		int currentMaxNums = 0;
		final Map<HSSFCell, Integer> map = new HashMap<HSSFCell, Integer>();
		LOGGER.debug("detecting column with substances");
		final HSSFWorkbook wb = getWorkbookFromFile(file);
		// TODO for now, we only look at sheet 0
		final HSSFSheet sheet = wb.getSheetAt(0);
		final Iterator<HSSFRow> i = sheet.rowIterator();
		while (i.hasNext()) {
			final HSSFRow r = (HSSFRow) i.next();
			final Iterator<HSSFCell> i2 = r.cellIterator();
			while (i2.hasNext()) {
				final HSSFCell c = (HSSFCell) i2.next();
				if(map.containsKey(c)){
					LOGGER.error("IllegalStateException", new IllegalStateException("cannot be"));
				} else {
					final HSSFCellFilter filter = new HSSFCellFilterOnlyStringCellValues();
					final int numOfCellsBelow = filter.filter(getCellsBelowCell(sheet, c)).size();
					LOGGER.debug("cell " + c.getRowIndex() + "," + c.getColumnIndex() + " has " + numOfCellsBelow + " cells below (string)");
					map.put(c, numOfCellsBelow);
				}
			}
		}	
		for(Entry<HSSFCell, Integer> e : map.entrySet()){
			final HSSFCell xx = e.getKey();
			final int num = e.getValue();
			if(num > currentMaxNums){
				currentMaxNums = num;
				result = xx.getColumnIndex();
			}
		}
		return result;
	}

	private static Collection<HSSFCell> getCellsBelowCell(HSSFSheet sheet, HSSFCell cell) {
		final Collection<HSSFCell> result = new ArrayList<HSSFCell>();
		final Iterator<HSSFRow> i = sheet.rowIterator();
		while (i.hasNext()) {
			final HSSFRow r = (HSSFRow) i.next();
			final Iterator<HSSFCell> i2 = r.cellIterator();
			while (i2.hasNext()) {
				final HSSFCell c = (HSSFCell) i2.next();
				if(c.getColumnIndex() == cell.getColumnIndex() && c.getRowIndex() > cell.getRowIndex()){
					result.add(c);
				} else {
					// ignore
				}
			}
		}
		return result;
	}

	public static HSSFCell detectLaborCell(File file)
			throws FileNotFoundException, IOException, FailedToDetectException {
		final Set<HSSFCell> cells = new HashSet<HSSFCell>();
		final Pattern p = Pattern.compile(".*labor.+nr.*",
				Pattern.CASE_INSENSITIVE);
		LOGGER.debug("detecting cell with labor identifier");
		final HSSFWorkbook wb = getWorkbookFromFile(file);
		// TODO for now, we only look at sheet 0
		final HSSFSheet sheet = wb.getSheetAt(0);
		final Iterator<HSSFRow> i = sheet.rowIterator();
		while (i.hasNext()) {
			final HSSFRow r = (HSSFRow) i.next();
			final Iterator<HSSFCell> i2 = r.cellIterator();
			while (i2.hasNext()) {
				final HSSFCell c = (HSSFCell) i2.next();
				if (c.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					final Matcher m = p.matcher(c.getRichStringCellValue()
							.getString());
					if (m.matches()) {
						LOGGER.debug("found match "
								+ c.getRichStringCellValue().getString());
						cells.add(c);
					} else {
						// ignore
					}
				}
			}
		}
		// TODO only consider first found cell;
		if (cells.isEmpty() || cells.size() > 1)
			throw new FailedToDetectException(
					"could not auto-detect cell containing labor ident string. number of found cells="
							+ cells.size());
		return cells.iterator().next();
	}

	public static HSSFCell detectProbeCell(File file)
			throws FileNotFoundException, IOException, FailedToDetectException {
		final Set<HSSFCell> cells = new HashSet<HSSFCell>();
		final Pattern p = Pattern.compile(".*probe.+nr.*",
				Pattern.CASE_INSENSITIVE);
		LOGGER.debug("detecting cell with probe identifier");
		final HSSFWorkbook wb = getWorkbookFromFile(file);
		// TODO for now, we only look at sheet 0
		final HSSFSheet sheet = wb.getSheetAt(0);
		final Iterator<HSSFRow> i = sheet.rowIterator();
		while (i.hasNext()) {
			final HSSFRow r = (HSSFRow) i.next();
			// LOGGER.debug("now row " + r);
			final Iterator<HSSFCell> i2 = r.cellIterator();
			while (i2.hasNext()) {
				final HSSFCell c = (HSSFCell) i2.next();
				// LOGGER.debug("now cell " + c);
				if (c.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					final Matcher m = p.matcher(c.getRichStringCellValue()
							.getString());
					if (m.matches()) {
						LOGGER.debug("found match "
								+ c.getRichStringCellValue().getString());
						cells.add(c);
					} else {
						// ignore
					}
				}
			}
		}
		// TODO only consider first found cell;
		if (cells.isEmpty() || cells.size() > 1)
			throw new FailedToDetectException(
					"could not auto-detect cell containing probe ident string. number of found cells="
							+ cells.size());
		return cells.iterator().next();
	}

	public static void writeOutSubstance(final OutSubstance s, final File file)
			throws IOException {
		LOGGER.debug("writing file " + file);
		final HSSFWorkbook wb = new HSSFWorkbook();
		final HSSFSheet sheet = wb.createSheet();

		int currentRow = 5;
		for (OutSubstanceEntry entry : s.getEntries()) {
			writeProbeNr(s.getProbeIdent(), sheet);
			writeHeaderRow(sheet, entry.getValues().size());

			final HSSFRow valueRow = sheet.createRow(currentRow);
			// System.out.println(s);
			int currentColumn = 3;
			for (String value : entry.getValues()) {
				final HSSFCell laborNameCell = valueRow.createCell(1);
				laborNameCell.setCellValue(new HSSFRichTextString(entry
						.getLabor().getIdentifier()));
				LOGGER.debug("writing value=" + value + " to row=" + currentRow
						+ ", col=" + currentColumn);
				final HSSFCell valueCell = valueRow.createCell(currentColumn);
				valueCell.setCellValue(new HSSFRichTextString(value));
				currentColumn++;
			}
			System.out.println();
			currentRow++;
		}

		FileOutputStream fs = new FileOutputStream(file);
		wb.write(fs);
		// fs.flush();
		fs.close();

	}

	// TODO eliminate hardcoding
	private static void writeHeaderRow(HSSFSheet sheet, int length) {
		final HSSFRow row = sheet.createRow(HEADER_ROW);
		final HSSFCell c0 = row.createCell(0);
		c0.setCellValue(new HSSFRichTextString("Substanz"));
		final HSSFCell c1 = row.createCell(1);
		c1.setCellValue(new HSSFRichTextString("Lname"));

		for (int i = 3; i < 3 + length; i++) {
			final HSSFCell cell = row.createCell(i);
			cell.setCellValue(new HSSFRichTextString(Integer.toString(i - 2)));
		}

	}

	private static void writeProbeNr(String ident, HSSFSheet sheet) {
		final HSSFRow row = sheet.createRow(PROBE_IDENT_ROW);
		final HSSFCell cell = row.createCell(PROBE_IDENT_COL);
		cell.setCellValue(new HSSFRichTextString(new StringBuilder().append(
				PROBE_IDENT_PREFIX).append(ident).append(PROBE_IDENT_POSTFIX)
				.toString()));
	}

	private final static int PROBE_IDENT_ROW = 2;
	private final static int PROBE_IDENT_COL = 0;
	private final static String PROBE_IDENT_PREFIX = "Probe Nr.: ";
	private final static String PROBE_IDENT_POSTFIX = "";
	private final static int HEADER_ROW = PROBE_IDENT_ROW + 2;

	

	

}
