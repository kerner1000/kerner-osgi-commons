package core;

import interfaces.ReadWritePOI;
import interfaces.RingversuchGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import Exceptions.DublicateLaboratoryException;
import Exceptions.EmptyFileException;
import Exceptions.EmptySubstanceNameException;
import Exceptions.LaboratoryNotFoundException;
import Exceptions.ProbeNoNotFoundException;
import Exceptions.SheetNotFoundException;

public class Read implements ReadWritePOI {

	// A reference to GUI, to call function to append to log
	private RingversuchGUI gui;

	// Column with laboratory identification
	private int laboratoryColumn;

	// Row with laboratory identification
	private int laboratoryRow;

	// Row, that contains first ...
	private int valuesStartRow;

	private int probeNoRow;

	private int probeNoColumn;

	private int valuesEndColumn;

	private int valuesStartColumn;

	private int sheetNo;

	// desired probe no,
	private int probeNo;

	private int substancesColumn;

	private int valuesEndRow;

	// list of all laboratories
	private ArrayList<String> laboratories;

	// names of substances
	private ArrayList<String> columnTitles;

	// contains all values to each substance
	private HashMap<String, ArrayList<Double>> values;

	// key: laboratory identification + "@" + substance.
	// value: values
	// contains all values from all substances form all laboratories
	private HashMap<String, ArrayList<Double>> labval;

	// are we debugging?
	private final boolean debug = false;

	public Read(RingversuchGUI gui) {
		this.gui = gui;
		laboratories = new ArrayList<String>();
		columnTitles = new ArrayList<String>();
		values = new HashMap<String, ArrayList<Double>>();
		labval = new HashMap<String, ArrayList<Double>>();
	}

	/**
	 * Tries to get probe no.
	 * 
	 * @param sheet
	 *            sheet, we are reading from.
	 * @return probe no
	 * @throws ProbeNoNotFoundException
	 */
	private int getProbeNo(HSSFSheet sheet) throws ProbeNoNotFoundException {

		HSSFRow row = sheet.getRow(probeNoRow);
		HSSFCell cell = row.getCell((short) probeNoColumn);
		String probe = cell.toString();
		if (debug)
			System.out.println("Read.getProbeNo: probeString=" + probe);

		try {

			return (int) Double.parseDouble(probe);
		} catch (NumberFormatException e) {
			if (debug)
				e.printStackTrace();
			throw new ProbeNoNotFoundException();
		}

	}

	/**
	 * Tries to get current laboratory identification.
	 * 
	 * @param sheet
	 *            sheet, we are reading from.
	 * 
	 * @return laboratory identification string
	 * @throws LaboratoryNotFoundException
	 * @throws EmptyFileException
	 */
	private String getLaboratory(HSSFSheet sheet)
			throws LaboratoryNotFoundException, EmptyFileException {

		try {

			HSSFRow row = sheet.getRow(laboratoryRow);
			HSSFCell cell = row.getCell((short) laboratoryColumn);
			if (debug)
				System.out.println("Read.getLaboratory: laboratory="
						+ cell.toString());

			if (cell.toString().isEmpty())
				throw new LaboratoryNotFoundException();

			return cell.toString();
		} catch (NullPointerException e) {
			if (debug)
				e.printStackTrace();
			throw new EmptyFileException();
		}
	}

	private HSSFSheet getSheet(File file, int sheetNo)
			throws FileNotFoundException, IOException, SheetNotFoundException {
		// POI-file system representation of current file
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));

		// current workbook we are reading from
		HSSFWorkbook wb = new HSSFWorkbook(fs);

		try {
			// current sheet we are reading from
			HSSFSheet sheet = wb.getSheetAt(sheetNo);
			return sheet;
		} catch (IndexOutOfBoundsException e) {
			if (debug)
				e.printStackTrace();
			throw new SheetNotFoundException("no sheet no " + (sheetNo + 1)
					+ " found! skipping file \"" + file.getName() + "\"");
		}

	}

	/**
	 * Get substances names
	 * 
	 * @param sheet
	 *            sheet, we are reading from.
	 * @return An ArrayList with column titles. (Description of probe)
	 * @throws EmptyFileException
	 */
	private ArrayList<String> getColumnTitles(HSSFSheet sheet, File file)
			throws EmptyFileException {

		try {
			ArrayList<String> list = new ArrayList<String>();
			for (int i = valuesStartColumn; i <= valuesEndColumn; i++) {
				HSSFRow row = sheet.getRow(valuesStartRow - 1);
				HSSFCell cell = row.getCell((short) i);
				list.add(cell.toString().replaceAll("\n", " "));
			}

			if (debug)
				System.out.println("Read.getColumnTitles: " + list);
			return list;
		} catch (NullPointerException e) {
			if (debug)
				e.printStackTrace();
			throw new EmptyFileException();
		}
	}

	public int readFile(File file) throws FileNotFoundException, IOException {

		int skippedFiles = 0;

		try {

			HSSFSheet sheet = getSheet(file, sheetNo);

			// get current laboratory information
			String laboratory = getLaboratory(sheet);

			// only proceed, if probe no is desired one
			if (getProbeNo(sheet) == probeNo) {

				if (laboratories.contains(laboratory)) {
					throw new DublicateLaboratoryException();
				} else
					laboratories.add(laboratory);

				// get substances names
				columnTitles = getColumnTitles(sheet, file);

				// collect values
				collectValues(sheet, file, laboratory);

			} else {
				gui.appendLog("skipped file \"" + file.getName()
						+ "\" due to wrong probe no.\n", false);
				skippedFiles++;
			}
		} catch (Exception e) {
			if (debug)
				e.printStackTrace();
			gui.appendLog(e.getMessage() + " .. Skipping file \""
					+ file.getName() + "\"\n", false);
			skippedFiles++;
		}
		return skippedFiles;

	}

	private void collectValues(HSSFSheet sheet, File file, String laboratory)
			throws EmptySubstanceNameException {
		// tmp
		HSSFRow row;
		HSSFCell cell;
		String substance = null;
		double value = 0.0;

		for (int i = valuesStartRow; i <= valuesEndRow; i++) {
			for (int j = valuesStartColumn; j <= valuesEndColumn; j++) {

				// select cell at i, j
				row = sheet.getRow(i);
				cell = row.getCell((short) j);

				if (debug) {
					System.out.println("Read.collectValues.cell: "
							+ cell.toString());
					// System.out.println("Read.collectValues.cell: " +
					// cell.getNumericCellValue());
				}

				// new substances column: get substance name
				if (j == valuesStartColumn) {

					// select cell with substance name
					HSSFCell substCell = row
							.getCell((short) (substancesColumn));

					// get substance string
					substance = substCell.toString();
					if (debug)
						System.out.println("Read.collectValues.substance: "
								+ substance);

					if (substance.isEmpty())
						throw new EmptySubstanceNameException(
								"Empty substance name received! Aborting.\n"
										+ file);

					// new substance: add to list of values
					if (!values.containsKey(substance)) {
						values.put(substance, new ArrayList<Double>());
					}

					// new substance: add to list of values
					if (!labval.containsKey(laboratory + "@" + substance)) {
						labval.put(laboratory + "@" + substance,
								new ArrayList<Double>());
					}
				}
				// getNumericCellValue() liefert den
				// Abgebildeten Zahlenwert und nicht die
				// dahinter liegende Formel

				// if cell is empty, value is zero
				if (debug)
					System.out.println("Read.collectValues: cell number: " + i
							+ ", " + j + ", " + cell.toString());
				try {
					value = cell.getNumericCellValue();
				} catch (NumberFormatException e) {
					value = 0.0;
					if (debug)
						e.printStackTrace();
					gui.appendLog("invalid cell content in \"" + file
							+ "\"\n at row " + i + ", column " + j + " (value:\""+cell+"\")"
							+ ", assuming 0.0\n", false);
					
				} finally {
					values.get(substance).add(value);
					labval.get(laboratory + "@" + substance).add(value);
				}

			}
		}
	}

	@SuppressWarnings("deprecation")
	public void writeFiles(File path) throws FileNotFoundException, IOException {
		try {
			
			// Variable fuer die Startspalte der Ausgabe
			int startbesch = 0;

			// Arbeitsmappe
			HSSFWorkbook wb = new HSSFWorkbook();
			// Tabelle erstellen
			HSSFSheet s = wb.createSheet();
			// Zellengroesse
			s.setColumnWidth((short) 0, (short) 5500);
			s.setColumnWidth((short) 2, (short) 3000);
			// Reihen und Spalten Objekte erstellen
			HSSFRow r = null;
			HSSFRow r2 = null;
			HSSFCell sb = null;
			HSSFCell va = null;
			HSSFCell pr = null;
			HSSFCell ue = null;
			HSSFCell am = null;

			// creating a custom palette for the workbook
			HSSFPalette palette = wb.getCustomPalette();
			palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 194, // RGB
					// red
					(byte) 214, // RGB green
					(byte) 154 // RGB blue
					);

			// Fontdaten Stil usw. Objekte erstellen
			// Fettschrift mit gruenem Hintergrund und fetter Umrandung
			HSSFFont fnt1 = wb.createFont();
			fnt1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle bo = wb.createCellStyle();
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
			HSSFFont fnt = wb.createFont();
			fnt.setColor(HSSFFont.COLOR_NORMAL);
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setFont(fnt);
			cs.setWrapText(true);

			// Fettschrift
			HSSFFont fnt2 = wb.createFont();
			fnt2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle fs = wb.createCellStyle();
			fs.setFont(fnt2);
			fs.setWrapText(true);

			// Proben Nr.
			r2 = s.createRow(2);
			pr = r2.createCell((short) 0, HSSFCell.CELL_TYPE_STRING);
			pr.setCellValue("Probe Nr.: " + probeNo);
			pr.setCellStyle(fs);

			if (debug)
				System.out.println("Read.writeFiles: labval: " + labval);

			Vector<String> alreadyCreated = new Vector<String>();

			// >> ALLE SUBSTANZEN DURCHGEHEN <<
			for (String substanz : labval.keySet()) {

				String tmp[] = new String[4];

				tmp = substanz.split("@");
				substanz = tmp[1];

				if (alreadyCreated.contains(substanz)) {

				}

				else {

					alreadyCreated.add(substanz);

					if (debug)
						System.out.println("Read.writeFiles: substanz: "
								+ substanz);

					// Ausgabedatei erstellen - Seperator ist das
					// plattformunabhaengige Zeichen
					String fileName = path.getAbsolutePath()
					+ File.separator + probeNo + "_" + Core.formatToValidString(substanz)
					+ ".xls";
					File file = new File(fileName);

					FileOutputStream out = new FileOutputStream(file);

					// Reihen und Spalten fuellen mit Name und Werten
					// Ueberschrifen erstellen
					startbesch = 0;
					r = s.createRow(4);
					// Zellenbeschriftung Substanz
					ue = r.createCell((short) startbesch,
							HSSFCell.CELL_TYPE_STRING);
					ue.setCellValue("Substanz");
					ue.setCellStyle(bo);

					// Zellenbeschriftung Lname
					startbesch++;
					ue = r.createCell((short) startbesch,
							HSSFCell.CELL_TYPE_STRING);
					ue.setCellValue("Lname");
					ue.setCellStyle(bo);

					// Zellenbeschriftung Info
					startbesch++;
					ue = r.createCell((short) startbesch,
							HSSFCell.CELL_TYPE_STRING);
					ue.setCellValue("Info");
					ue.setCellStyle(bo);

					// Je nach Menge Best.
					startbesch++;
					int zz = startbesch;
					for (int z = 1; z <= (valuesEndColumn - valuesStartColumn) + 1; z++) {
						ue = r.createCell((short) zz++,
								HSSFCell.CELL_TYPE_STRING);
						ue.setCellValue(columnTitles.get(z - 1) + z);
						ue.setCellStyle(bo);
					}

					// LABORNUMMERN SORTIEREN
					// Comparator<String> comparator =
					// Collections.<String>reverseOrder();
					// Collections.sort(labnbrs, comparator);
					// System.out.println(labnbrs);

					// ALLE LABORE DURCHGEHEN
					// Schleife fuer alle Labore
					short rownum = 5;
					for (String key : labval.keySet()) {
						// Nur eine Substanz waehlen
						tmp = key.split("@");
						if (tmp[1].equals(substanz)) {
							// Die einzelnen Werte der Substanzen ausgeben
							short cellnum = 3;
							for (int n = 0; n < (labval.get(key).size()); n++) {
								r = s.createRow(rownum);
								// SUBSTANZ
								sb = r.createCell((short) (0),
										HSSFCell.CELL_TYPE_STRING);

								// LABNAME
								pr = r.createCell((short) (1),
										HSSFCell.CELL_TYPE_STRING);

								// INFOFELD
								am = r.createCell((short) (2),
										HSSFCell.CELL_TYPE_STRING);

								// BESTFELDER
								va = r.createCell((cellnum++),
										HSSFCell.CELL_TYPE_NUMERIC);

								// Inhalt zuweisen
								sb.setCellValue(substanz);
								pr.setCellValue(tmp[0]);
								am.setCellValue("");
								// Kontrolle Inhalt
								va.setCellValue(Double.parseDouble(labval.get(
										key).get(n).toString()));
							}
							rownum++;
						}

						// Oben definierten Cellstyle auf Zelle ÃŒbertragen
						sb.setCellStyle(fs);
						va.setCellStyle(cs);
						// prn.setCellStyle(cs);
					}
					// Daten in out-Stream schreiben und out schlieÃen
					if (debug)
						System.out.println("Read.writeFiles: "
								+ "writing file " + file.getName());
					// Ausgabe Logfenster
					gui.appendLog("writing file " + file.getName() + "\n",
							false);
					wb.write(out);
					out.close();

				}
			}
		} catch (IOException io) {
			if(debug)
				io.printStackTrace();
			System.out.println(io);
			gui.appendLog("ERROR: " + io.getMessage() + "\n", true);
		}
	}

	public void setLaboratoryColumn(int laboratoryColumn) {
		this.laboratoryColumn = laboratoryColumn;
		if (debug)
			System.out.println("Read.setLaboratoryColumn: " + laboratoryColumn);
	}

	public void setLaboratoryRow(int laboratoryRow) {
		this.laboratoryRow = laboratoryRow;
		if (debug)
			System.out.println("Read.setLaboratoryRow: " + laboratoryRow);
	}

	public void setProbeNo(int probeNo) {
		this.probeNo = probeNo;
		if (debug)
			System.out.println("Read.setProbeNo: " + probeNo);
	}

	public void setProbeNoColum(int probeNoColumn) {
		this.probeNoColumn = probeNoColumn;
		if (debug)
			System.out.println("Read.setProbeNoColumn: " + probeNoColumn);

	}

	public void setProbeNoRow(int probeNoRow) {
		this.probeNoRow = probeNoRow;
		if (debug)
			System.out.println("Read.setProbeNoRow: " + probeNoRow);
	}

	public void setValuesEndColumn(int valuesEndColumn) {
		this.valuesEndColumn = valuesEndColumn;
		if (debug)
			System.out.println("Read.setValuesEndColumn: " + valuesEndColumn);
	}

	public void setValuesEndRow(int valuesEndRow) {
		this.valuesEndRow = valuesEndRow;
		if (debug)
			System.out.println("Read.setValuesEndRow: " + valuesEndRow);
	}

	public void setValuesStartColumn(int valuesStartColumn) {
		this.valuesStartColumn = valuesStartColumn;
		if (debug)
			System.out.println("Read.setValuesStartColumn: "
					+ valuesStartColumn);

	}

	public void setValuesStartRow(int valuesStartRow) {
		this.valuesStartRow = valuesStartRow;
		if (debug)
			System.out.println("Read.setValuesStartRow: " + valuesStartRow);

	}

	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
		if (debug)
			System.out.println("Read.setSheetNo: " + sheetNo);

	}

	public void setColumnOfSubstances(int substancesColumn) {
		this.substancesColumn = substancesColumn;
		if (debug)
			System.out.println("Read.setColumnOfSubstances: "
					+ substancesColumn);

	}

}