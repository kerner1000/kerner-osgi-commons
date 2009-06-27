package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * !!! MAKE HIM THREADSAVE !!!
 *
 */
public class SettingsManagerImpl implements SettingsManager {

	private final static Logger LOGGER = Logger
			.getLogger(SettingsManagerImpl.class);
	private final static String SETTINGS_HEADER = "This is a settings file for \"Ringversuch-v.x.x.jar\"";
	private final static String LABOR_NO_ROW = "labor.no.row";
	private int laborNoRow = -1;
	private final static String LABOR_NO_COLUMN = "labor.no.column";
	private int laborNoColumn = -1;
	private final static String PROBE_NO_ROW = "probe.no.row";
	private int probeNoRow = -1;
	private final static String PROBE_NO_COLUMN = "probe.no.column";
	private int probeNoCOlumn = -1;
	private final static String PROBE_VALUE = "probe.ident";
	private String probeValue = null;
	private final static String SHEET_NO = "sheet.no";
	private int sheetNo = -1;
	private final static String SUBSTANCES_COLUMN = "substances.column";
	private int substancesColumn = -1;
	private final static String VALUES_START_ROW = "values.start.row";
	private int valuesStartRow = -1;
	private final static String VALUES_START_COLUMN = "values.start.column";
	private int valuesStartColumn = -1;
	private final static String VALUES_END_ROW = "values.end.row";
	private int valuesEndRow = -1;
	private final static String VALUES_END_COLUMN = "values.end.column";
	private int valuesEndColumn = -1;

	private Properties currentSettings = getDefaultSettings();

	public static final SettingsManagerImpl INSTANCE = new SettingsManagerImpl();

	private SettingsManagerImpl() {
		
	}

	@Override
	public synchronized void loadSettings(final File file) throws IOException,
			InvalidSettingsException {
		final FileInputStream in = new FileInputStream(file);
		currentSettings.load(in);
		in.close();
		mergeSettings();
	}

	private void mergeSettings() throws InvalidSettingsException {
		setLaborNoRow(currentSettings.getProperty(LABOR_NO_ROW));
		setLaborNoColumn(currentSettings.getProperty(LABOR_NO_COLUMN));
		setProbeNoRow(currentSettings.getProperty(PROBE_NO_ROW));
		setProbeNoColumn(currentSettings.getProperty(PROBE_NO_COLUMN));
		setProbeValue(currentSettings.getProperty(PROBE_VALUE));
		setSheetNo(currentSettings.getProperty(SHEET_NO));
		setSubstancesColumn(currentSettings.getProperty(SUBSTANCES_COLUMN));
		setValuesStartRow(currentSettings.getProperty(VALUES_START_ROW));
		setValuesStartColumn(currentSettings.getProperty(VALUES_START_COLUMN));
		setValuesEndRow(currentSettings.getProperty(VALUES_END_ROW));
		setValuesEndColumn(currentSettings.getProperty(VALUES_END_COLUMN));
	}

	private void setValuesEndColumn(String string)
			throws InvalidSettingsException {
		final int i = convert(string);
		if (i > 0)
			valuesEndColumn = i - 1;
		else
			throw new InvalidSettingsException(
					"Column of values end invalid for value " + string);

	}

	private void setValuesEndRow(String string) throws InvalidSettingsException {
		int i = -1;
		i = Integer.parseInt(string);
		if (i > 0)
			valuesEndRow = i - 1;
		else
			throw new InvalidSettingsException(
					"Row of values end invalid for value " + string);

	}

	private void setValuesStartColumn(String string)
			throws InvalidSettingsException {
		final int i = convert(string);
		if (i > 0)
			valuesStartColumn = i - 1;
		else
			throw new InvalidSettingsException(
					"Column of values start invalid for value " + string);
	}

	private void setValuesStartRow(String string)
			throws InvalidSettingsException {
		int i = -1;
		i = Integer.parseInt(string);
		if (i > 0)
			valuesStartRow = i - 1;
		else
			throw new InvalidSettingsException(
					"Row of values start invalid for value " + string);
	}

	private void setSubstancesColumn(String string)
			throws InvalidSettingsException {
		final int i = convert(string);
		if (i > 0)
			substancesColumn = i - 1;
		else
			throw new InvalidSettingsException(
					"Column of substances invalid for value " + string);
	}

	private void setSheetNo(String string) throws InvalidSettingsException {
		int i = -1;
		i = Integer.parseInt(string);
		if (i > 0)
			sheetNo = i - 1;
		else
			throw new InvalidSettingsException("Sheet no invalid for value "
					+ string);
	}

	private void setProbeValue(String string) {
		// TODO defensive copying ?
		probeValue = string;
	}

	private void setProbeNoColumn(String string)
			throws InvalidSettingsException {
		final int i = convert(string);
		if (i > 0)
			probeNoCOlumn = i - 1;
		else
			throw new InvalidSettingsException(
					"Column of probe identifier invalid for value " + string);
	}

	private void setProbeNoRow(String string) throws InvalidSettingsException {
		int i = -1;
		i = Integer.parseInt(string);
		if (i > 0)
			probeNoRow = i - 1;
		else
			throw new InvalidSettingsException(
					"Row of probe identifier invalid for value " + string);
	}

	private void setLaborNoColumn(final String string)
			throws InvalidSettingsException {
		final int i = convert(string);
		if (i > 0)
			laborNoColumn = i - 1;
		else
			throw new InvalidSettingsException(
					"Column of labor identifier invalid for value " + string);
	}

	private void setLaborNoRow(final String string)
			throws InvalidSettingsException {
		int i = -1;
		try{
		i = Integer.parseInt(string);
		}catch(NumberFormatException e){
			throw new InvalidSettingsException(
					"Row of labor identifier invalid for value " + string);
		}
		if (i > 0)
			laborNoRow = i - 1;
		else
			throw new InvalidSettingsException(
					"Row of labor identifier invalid for value " + string);
	}

	public synchronized void saveSettings(final File file) throws IOException {
		String path = file.getAbsolutePath();
		if (path.endsWith((".ini"))) {
			path = path.substring(0, path.lastIndexOf('.'));
		}
		path = path + ".ini";
		final FileOutputStream out = new FileOutputStream(path);
		currentSettings.store(out, SETTINGS_HEADER);
		out.close();
	}

	private Properties getDefaultSettings() {
		final Properties defaultSettings = new Properties();
		defaultSettings.setProperty(SettingsManagerImpl.LABOR_NO_ROW, "e.g. 1");
		defaultSettings.setProperty(SettingsManagerImpl.LABOR_NO_COLUMN, "e.g. A");
		defaultSettings.setProperty(SettingsManagerImpl.PROBE_NO_ROW, "e.g. 1");
		defaultSettings.setProperty(SettingsManagerImpl.PROBE_NO_COLUMN, "e.g. A");
		defaultSettings.setProperty(SettingsManagerImpl.PROBE_VALUE, "e.g. 1");
		defaultSettings.setProperty(SettingsManagerImpl.SHEET_NO, "e.g. 1");
		defaultSettings
				.setProperty(SettingsManagerImpl.SUBSTANCES_COLUMN, "e.g. A");
		defaultSettings.setProperty(SettingsManagerImpl.VALUES_START_ROW, "e.g. 1");
		defaultSettings.setProperty(SettingsManagerImpl.VALUES_START_COLUMN,
				"e.g A");
		defaultSettings.setProperty(SettingsManagerImpl.VALUES_END_ROW, "e.g. 1");
		defaultSettings.setProperty(SettingsManagerImpl.VALUES_END_COLUMN, "e.g A");
		return defaultSettings;
	}

	private boolean isValidChar(int i) {
		if (i > 0 && i < 27)
			return true;
		else
			return false;
	}

	/**
	 * Convert a Character representation [a-z,A-Z] to according integer value
	 * 
	 * @param string
	 * @return
	 * @throws InvalidSettingsException
	 */
	private int convert(String string) throws InvalidSettingsException {
		int i = -1;
		char c = 'a';
		if (string.length() > 1) {
			LOGGER.debug("unexpected string length: string: " + string);
		}
		c = string.toUpperCase().charAt(0);
		i = (int) c;
		i = i - 64;
		if (isValidChar(i))
			return i;
		else {
			final InvalidSettingsException e = new InvalidSettingsException(
					"Invalid character for value " + i);
			LOGGER.debug("Invalid character for value " + i, e);
			throw e;
		}
	}

	@Override
	public int getProbeRowIndex() {
		return probeNoRow;
	}

	@Override
	public int getProbeColumnIndex() {
		return probeNoCOlumn;
	}

	@Override
	public int getLaborColumnIndex() {
		return laborNoColumn;
	}

	@Override
	public int getLaborRowIndex() {
		return laborNoRow;
	}

	@Override
	public int getValuesStartColumnIndex() {
		return valuesStartColumn;
	}

	@Override
	public int getValuesStartRowIndex() {
		return valuesStartRow;
	}

	@Override
	public int getValuesEndColumnIndex() {
		return valuesEndColumn;
	}

	@Override
	public int getValuesEndRowIndex() {
		return valuesEndRow;
	}

	@Override
	public int getSubstancesColumnIndex() {
		return substancesColumn;
	}

	@Override
	public String getProbeIdent() {
		return probeValue;
	}
}
