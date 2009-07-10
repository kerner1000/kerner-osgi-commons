package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;

/**
 * 
 * !!! MAKE HIM THREADSAVE !!!
 * 
 */
public class SettingsManagerImpl implements SettingsManager {

	private final static Logger LOGGER = Logger
			.getLogger(SettingsManagerImpl.class);
	private int laborNoRow = -1;
	private int laborNoColumn = -1;
	private int probeNoRow = -1;
	private int probeNoCOlumn = -1;
	private String probeValue = null;
	private int sheetNo = -1;
	private int substancesColumn = -1;
	private int valuesStartRow = -1;
	private int valuesStartColumn = -1;
	private int valuesEndRow = -1;
	private int valuesEndColumn = -1;

	public static final SettingsManager INSTANCE = new SettingsManagerImpl();

	private SettingsManagerImpl() {
		
	}

	@Override
	public synchronized Properties getCurrentProperties() {
		final Properties p = buildProperties();
		LOGGER.debug("current properties="+p);
		return p;
	}
	
	@Override
	public synchronized void setCurrentProperties(Properties properties) throws InvalidSettingsException{
		LOGGER.debug("setting properties="+properties);
		setLaborNoRow(properties.getProperty(Preferences.LABOR_NO_ROW));
		setLaborNoColumn(properties
				.getProperty(Preferences.LABOR_NO_COLUMN));
		setProbeNoRow(properties.getProperty(Preferences.PROBE_NO_ROW));
		setProbeNoColumn(properties
				.getProperty(Preferences.PROBE_NO_COLUMN));
		setProbeValue(properties.getProperty(Preferences.PROBE_VALUE));
		setSheetNo(properties.getProperty(Preferences.SHEET_NO));
		setSubstancesColumn(properties
				.getProperty(Preferences.SUBSTANCES_COLUMN));
		setValuesStartRow(properties
				.getProperty(Preferences.VALUES_START_ROW));
		setValuesStartColumn(properties
				.getProperty(Preferences.VALUES_START_COLUMN));
		setValuesEndRow(properties.getProperty(Preferences.VALUES_END_ROW));
		setValuesEndColumn(properties
				.getProperty(Preferences.VALUES_END_COLUMN));
	}

	@Override
	public synchronized Properties getDefaultProperties() {
		return getDefaultSettings();
	}

	@Override
	public synchronized void loadSettings(final File file) throws IOException,
			InvalidSettingsException {
		final FileInputStream in = new FileInputStream(file);
		final Properties p = new Properties();
		p.load(in);
		in.close();
		setCurrentProperties(p);
	}
	
	@Override
	public synchronized void saveSettings(final File file) throws IOException {
		String path = file.getAbsolutePath();
		if (path.endsWith((".ini"))) {
			path = path.substring(0, path.lastIndexOf('.'));
		}
		path = path + ".ini";
		final FileOutputStream out = new FileOutputStream(path);
		final Properties p = new Properties(getCurrentProperties());
		p.store(out, Preferences.SETTINGS_HEADER);
		out.close();
	}

	private Properties buildProperties() {
		final Properties settings = new Properties();
		settings.setProperty(Preferences.LABOR_NO_ROW, Integer
				.toString(laborNoRow));
		settings.setProperty(Preferences.LABOR_NO_COLUMN, Integer
				.toString(laborNoColumn));
		settings.setProperty(Preferences.PROBE_NO_ROW, Integer
				.toString(probeNoRow));
		settings.setProperty(Preferences.PROBE_NO_COLUMN, Integer
				.toString(probeNoCOlumn));
		settings.setProperty(Preferences.PROBE_VALUE, probeValue);
		settings.setProperty(Preferences.SHEET_NO, Integer.toString(sheetNo));
		settings.setProperty(Preferences.SUBSTANCES_COLUMN, Integer
				.toString(substancesColumn));
		settings.setProperty(Preferences.VALUES_START_ROW, Integer
				.toString(valuesStartRow));
		settings.setProperty(Preferences.VALUES_START_COLUMN, Integer
				.toString(valuesStartColumn));
		settings.setProperty(Preferences.VALUES_END_ROW, Integer
				.toString(valuesEndRow));
		settings.setProperty(Preferences.VALUES_END_COLUMN, Integer
				.toString(valuesEndColumn));
		return settings;
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
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new InvalidSettingsException(
					"Row of labor identifier invalid for value " + string);
		}
		if (i > 0)
			laborNoRow = i - 1;
		else
			throw new InvalidSettingsException(
					"Row of labor identifier invalid for value " + string);
	}

	private Properties getDefaultSettings() {
		final Properties defaultSettings = new Properties();
		defaultSettings.setProperty(Preferences.LABOR_NO_ROW, "e.g. 1");
		defaultSettings.setProperty(Preferences.LABOR_NO_COLUMN, "e.g. A");
		defaultSettings.setProperty(Preferences.PROBE_NO_ROW, "e.g. 1");
		defaultSettings.setProperty(Preferences.PROBE_NO_COLUMN, "e.g. A");
		defaultSettings.setProperty(Preferences.PROBE_VALUE, "e.g. 1");
		defaultSettings.setProperty(Preferences.SHEET_NO, "e.g. 1");
		defaultSettings.setProperty(Preferences.SUBSTANCES_COLUMN, "e.g. A");
		defaultSettings.setProperty(Preferences.VALUES_START_ROW, "e.g. 1");
		defaultSettings.setProperty(Preferences.VALUES_START_COLUMN, "e.g A");
		defaultSettings.setProperty(Preferences.VALUES_END_ROW, "e.g. 1");
		defaultSettings.setProperty(Preferences.VALUES_END_COLUMN, "e.g A");
		return defaultSettings;
	}

	private boolean isValidChar(int i) {
		LOGGER.debug("checking char validity for integer " + i);
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
		LOGGER.debug("converting string " + string + " to integer");
		int i = -1;
		char c = 'a';
		if (string.length() > 1) {
			LOGGER.debug("unexpected string length: string: " + string);
		}
		c = string.toUpperCase().charAt(0);
		LOGGER.debug("character="+c);
		i = (int) c;
		LOGGER.debug("integer="+i);
		i = i - 64;
		LOGGER.debug("integer="+i);
		if (isValidChar(i))
			return i;
		else {
			final InvalidSettingsException e = new InvalidSettingsException(
					"Invalid character for value " + i);
			LOGGER.debug("Invalid character for value " + i, e);
			throw e;
		}
	}
	
	private String convert(int i) {
		LOGGER.debug("converting integer " + i + " to string");
		char c = (char) i;
		return Character.toString(c);
	}
}
