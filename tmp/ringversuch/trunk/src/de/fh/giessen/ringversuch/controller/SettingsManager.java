package de.fh.giessen.ringversuch.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SettingsManager {

	private final static Logger LOGGER = Logger
			.getLogger(SettingsManager.class);
	private final static String SETTINGS_HEADER = "This is a settings file for \"Ringversuch-v.x.x.jar\"";
	private final static String LABOR_NO_ROW = "labor.no.row";
	private final static String LABOR_NO_COLUMN = "labor.no.column";
	private final static String PROBE_NO_ROW = "probe.no.row";
	private final static String PROBE_NO_COLUMN = "labor.no.column";
	private final static String PROBE_VALUE = "probe.value";
	private final static String SHEET_NO = "sheet.no";
	private final static String SUBSTANCES_COLUMN = "substances.column";
	private final static String VALUES_START_ROW = "values.start.row";
	private final static String VALUES_START_COLUMN = "values.start.column";
	private final static String VALUES_END_ROW = "values.end.row";
	private final static String VALUES_END_COLUMN = "values.end.column";

	private final Controller controller;
	private Properties currentSettings = getDefaultSettings();

	SettingsManager(final Controller controller) {
		this.controller = controller;
	}
	
	synchronized Properties getSettings(){
		return new Properties(currentSettings);
	}

	 void loadSettings(File file) throws IOException {
		final FileInputStream in = new FileInputStream(file);
		currentSettings.load(in);
		in.close();
	}

	 void saveSettings(final File file) throws IOException {
		String path = file.getAbsolutePath();
		if (path.endsWith((".ini"))) {
			path = path.substring(0, path.lastIndexOf('.'));
		}
		path = path + ".ini";
		final FileOutputStream out = new FileOutputStream(path);
		currentSettings.store(out, SETTINGS_HEADER);
		out.close();
	}

	 void setSettings() {
		currentSettings.setProperty(SettingsManager.LABOR_NO_ROW, "e.g. 1");
		currentSettings.setProperty(SettingsManager.LABOR_NO_COLUMN, "e.g. A");
		currentSettings.setProperty(SettingsManager.PROBE_NO_ROW, "e.g. 1");
		currentSettings.setProperty(SettingsManager.PROBE_NO_COLUMN, "e.g. A");
		currentSettings.setProperty(SettingsManager.PROBE_VALUE, "e.g. 1");
		currentSettings.setProperty(SettingsManager.SHEET_NO, "e.g. 1");
		currentSettings
				.setProperty(SettingsManager.SUBSTANCES_COLUMN, "e.g. A");
		currentSettings.setProperty(SettingsManager.VALUES_START_ROW, "e.g. 1");
		currentSettings.setProperty(SettingsManager.VALUES_START_COLUMN,
				"e.g A");
		currentSettings.setProperty(SettingsManager.VALUES_END_ROW, "e.g. 1");
		currentSettings.setProperty(SettingsManager.VALUES_END_COLUMN, "e.g A");
	}

	 Properties getDefaultSettings() {
		final Properties defaultSettings = new Properties();
		defaultSettings.setProperty(SettingsManager.LABOR_NO_ROW, "e.g. 1");
		defaultSettings.setProperty(SettingsManager.LABOR_NO_COLUMN, "e.g. A");
		defaultSettings.setProperty(SettingsManager.PROBE_NO_ROW, "e.g. 1");
		defaultSettings.setProperty(SettingsManager.PROBE_NO_COLUMN, "e.g. A");
		defaultSettings.setProperty(SettingsManager.PROBE_VALUE, "e.g. 1");
		defaultSettings.setProperty(SettingsManager.SHEET_NO, "e.g. 1");
		defaultSettings
				.setProperty(SettingsManager.SUBSTANCES_COLUMN, "e.g. A");
		defaultSettings.setProperty(SettingsManager.VALUES_START_ROW, "e.g. 1");
		defaultSettings.setProperty(SettingsManager.VALUES_START_COLUMN,
				"e.g A");
		defaultSettings.setProperty(SettingsManager.VALUES_END_ROW, "e.g. 1");
		defaultSettings.setProperty(SettingsManager.VALUES_END_COLUMN, "e.g A");
		return defaultSettings;
	}

	 boolean settingsValid() {

		final String laborNoRow = currentSettings.getProperty(LABOR_NO_ROW);
		final String laborNoColumn = currentSettings.getProperty(LABOR_NO_COLUMN);
		final String probeNoRow = currentSettings.getProperty(PROBE_NO_ROW);
		final String probeNoColumn = currentSettings.getProperty(PROBE_NO_COLUMN);
		final String probeValue = currentSettings.getProperty(PROBE_VALUE);
		final String sheetNo = currentSettings.getProperty(SHEET_NO);
		final String substancesColumn = currentSettings.getProperty(SUBSTANCES_COLUMN);
		final String valuesStartRow = currentSettings.getProperty(VALUES_START_ROW);
		final String valuesStartColumn = currentSettings
				.getProperty(VALUES_START_COLUMN);
		final String valuesEndRow = currentSettings.getProperty(VALUES_END_ROW);
		final String valuesEndColumn = currentSettings.getProperty(VALUES_END_COLUMN);

		if (isValidInt(laborNoRow) == true
				|| isValidChar(convert(laborNoColumn)) == true
				|| isValidInt(probeNoRow) == true
				|| isValidChar(convert(probeNoColumn)) == true
				|| isValidInt(sheetNo) == true
				|| isValidChar(convert(substancesColumn)) == true
				|| isValidInt(valuesEndRow) == true
				|| isValidChar(convert(valuesEndColumn)) == true
				|| isValidInt(valuesStartRow) == true
				|| isValidChar(convert(valuesStartColumn)) == true
				|| isValidInt(probeValue) == true)
			return true;
		else
			return false;
	}

	private boolean isValidChar(int i) {
		boolean b = false;
		if (i > 0 && i < 27)
			b = true;
		else {
			LOGGER.debug("invalid char: " + i);
			controller.printMessage("Use characters between A and Z \n" + i,
					true);
		}
		return b;
	}

	private int convert(String string) {
		int i = -1;
		char c = 'a';
		if (string.length() > 1) {
			LOGGER.debug("unexpected string length: string: " + string);
			controller.showError("There may be problems with your settings");
		}
		c = string.toUpperCase().charAt(0);
		i = (int) c;
		i = i - 64;
		return i;
	}

	private boolean isValidInt(String string) {
		boolean b = false;
		int i = -1;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			LOGGER.debug("invalid integer value: ", e);
			return false;
		}
		if (i > 0)
			b = true;
		else {
			LOGGER.debug("invalid integer value: " + i);
			controller.printMessage("Use values above zero \n", true);
		}
		return b;
	}
}
