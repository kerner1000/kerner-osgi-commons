package de.fh.giessen.ringversuch.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.model.InvalidSettingsException;
import de.fh.giessen.ringversuch.model.SettingsModel;
import de.fh.giessen.ringversuch.model.SettingsModelImpl;
import de.fh.giessen.ringversuch.view.SettingsView;
import de.fh.giessen.ringversuch.view.SettingsViewImpl;

class SettingsConverter {

	private final static String SETTINGS_HEADER = "This is a settings file for \"Ringversuch-v.x.x.jar\"";
	private final static String LABOR_NO_ROW = "labor.no.row";
	private final static String LABOR_NO_COLUMN = "labor.no.column";
	private final static String PROBE_NO_ROW = "probe.no.row";
	private final static String PROBE_NO_COLUMN = "probe.no.column";
	private final static String PROBE_VALUE = "probe.ident";
	private final static String SUBSTANCES_COLUMN = "substances.column";
	private final static String VALUES_START_ROW = "values.start.row";
	private final static String VALUES_START_COLUMN = "values.start.column";
	private final static String VALUES_END_ROW = "values.end.row";
	private final static String VALUES_END_COLUMN = "values.end.column";
	private final static String SHEET_NO = "sheet.no";

	private final static Logger LOGGER = Logger
			.getLogger(SettingsConverter.class);

	private SettingsConverter() {
	}

	static Properties fileToProperties(File file) throws IOException {
		LOGGER.info("reading properties from file " + file);
		final FileInputStream in = new FileInputStream(file);
		final Properties properties = new Properties();
		try {
			properties.load(in);
		} finally {
			in.close();
		}
		LOGGER.debug("properties read=" + properties);
		return properties;
	}

	static void propertiesToFile(Properties properties, File file)
			throws IOException {
		LOGGER.debug(new StringBuilder().append("writing properties to file:")
				.append(Preferences.NEW_LINE).append("properties=").append(
						properties).append(", ").append("file=").append(file)
				.toString());
		String path = file.getAbsolutePath();
		if (path.endsWith((".ini"))) {
			path = path.substring(0, path.lastIndexOf('.'));
		}
		path = path + ".ini";
		final FileOutputStream out = new FileOutputStream(path);
		try {
			properties.store(out, SETTINGS_HEADER);
		} finally {
			out.close();
		}
	}

	/**
	 * In property file, syntax is like in settingsView, therefore we must
	 * convert it first to settingsView
	 * 
	 * @param properties
	 * @return
	 * @throws InvalidSettingsException
	 */
	static SettingsModel propertiesToModelSettings(Properties properties)
			throws InvalidSettingsException {
		LOGGER.debug("converting properties to model settings");
		return viewSettingsToModelSettings(propertiesToViewSettings(properties));
	}

	static SettingsView propertiesToViewSettings(Properties properties) {
		LOGGER.debug("converting properties to view settings");
		final SettingsView sv = new SettingsViewImpl();
		sv.setLaborIdentColumn(properties.getProperty(LABOR_NO_COLUMN));
		sv.setLaborIdentRow(properties.getProperty(LABOR_NO_ROW));
		sv.setProbeIdent(properties.getProperty(PROBE_VALUE));
		sv.setProbeIdentColumn(properties.getProperty(PROBE_NO_COLUMN));
		sv.setProbeIdentRow(properties.getProperty(PROBE_NO_ROW));
		sv.setSheetNo(properties.getProperty(SHEET_NO));
		sv.setSubstancesColumn(properties.getProperty(SUBSTANCES_COLUMN));
		sv.setValuesEndColumn(properties.getProperty(VALUES_END_COLUMN));
		sv.setValuesEndRow(properties.getProperty(VALUES_END_ROW));
		sv.setValuesStartColumn(properties.getProperty(VALUES_START_COLUMN));
		sv.setValuesStartRow(properties.getProperty(VALUES_START_ROW));
		LOGGER.debug(new StringBuilder().append(
				"converted proerties to view settings:").append(
				Preferences.NEW_LINE).append("properties=").append(properties)
				.append(", ").append("view settings=").append(sv));
		return sv;
	}

	static Properties settingsToProperties(SettingsModel settings) {
		final SettingsView sv = modelSettingsToViewSettings(settings);
		final Properties p = new Properties();
		p.setProperty(LABOR_NO_COLUMN, sv.getLaborIdentColumn());
		p.setProperty(LABOR_NO_ROW, sv.getLaborIdentRow());
		p.setProperty(PROBE_NO_COLUMN, sv.getProbeIdentColumn());
		p.setProperty(PROBE_NO_ROW, sv.getProbeIdentRow());
		p.setProperty(PROBE_VALUE, sv.getProbeIdent());
		p.setProperty(SHEET_NO, sv.getSheetNo());
		p.setProperty(SUBSTANCES_COLUMN, sv.getSubstancesColumn());
		p.setProperty(VALUES_END_COLUMN, sv.getValuesEndColumn());
		p.setProperty(VALUES_END_ROW, sv.getValuesEndRow());
		p.setProperty(VALUES_START_COLUMN, sv.getValuesStartColumn());
		p.setProperty(VALUES_START_ROW, sv.getValuesStartRow());
		return p;
	}

	static SettingsModel viewSettingsToModelSettings(SettingsView settings)
			throws InvalidSettingsException {
		final SettingsModel sm = new SettingsModelImpl();
		sm.setLaborIdentColumn(modelGetLaborIdentColumn(settings
				.getLaborIdentColumn()));
		sm.setLaborIdentRow(modelGetLaborIdentRow(settings.getLaborIdentRow()));
		sm.setProbeIdent(modelGetProbeIdent(settings.getProbeIdent()));
		sm.setProbeIdentColumn(modelGetProbeIdentColumn(settings
				.getProbeIdentColumn()));
		sm.setProbeIdentRow(modelGetProbeIdentRow(settings.getProbeIdentRow()));
		sm.setSheetNo(modelGetSheetNo(settings.getSheetNo()));
		sm.setSubstancesColumn(modelGetSubstancesColumn(settings
				.getSubstancesColumn()));
		sm.setValuesEndColumn(modelGetValuesEndColumn(settings
				.getValuesEndColumn()));
		sm.setValuesEndRow(modelGetValuesEndRow(settings.getValuesEndRow()));
		sm.setValuesStartColumn(modelGetValuesStartColumn(settings
				.getValuesStartColumn()));
		sm.setValuesStartRow(modelGetValuesStartRow(settings
				.getValuesStartRow()));
		return sm;
	}

	static SettingsView modelSettingsToViewSettings(SettingsModel settings) {
		final SettingsView sv = new SettingsViewImpl();
		sv.setLaborIdentColumn(viewGetLaborIdentColumn(settings
				.getLaborIdentColumn()));
		sv.setLaborIdentRow(viewGetLaborIdentRow(settings.getLaborIdentRow()));
		sv.setProbeIdent(viewGetProbeIdent(settings.getProbeIdent()));
		sv.setProbeIdentColumn(viewGetProbeIdentColumn(settings
				.getProbeIdentColumn()));
		sv.setProbeIdentRow(viewGetProbeIdentRow(settings.getProbeIdentRow()));
		sv.setSheetNo(viewGetSheetNo(settings.getSheetNo()));
		sv.setSubstancesColumn(viewGetSubstancesColumn(settings
				.getSubstancesColumn()));
		sv.setValuesEndColumn(viewGetValuesEndColumn(settings
				.getValuesEndColumn()));
		sv.setValuesStartColumn(viewGetValuesStartColumn(settings
				.getValuesStartColumn()));
		sv
				.setValuesStartRow(viewGetValuesStartRow(settings
						.getValuesStartRow()));
		sv.setValuesEndRow(viewGetValuesEndRow(settings.getValuesEndRow()));
		return sv;
	}

	private static String viewGetValuesEndRow(int i) {
		return Integer.toString(i + 1);
	}

	private static String viewGetValuesStartRow(int i) {
		return Integer.toString(i + 1);
	}

	private static String viewGetValuesStartColumn(int i) {
		return intToString(i + 1);
	}

	private static String viewGetValuesEndColumn(int i) {
		return intToString(i + 1);
	}

	private static String viewGetSubstancesColumn(int i) {
		return intToString(i + 1);
	}

	private static String viewGetSheetNo(int i) {
		return Integer.toString(i + 1);
	}

	private static String viewGetProbeIdentRow(int i) {
		return Integer.toString(i + 1);
	}

	private static String viewGetProbeIdentColumn(int i) {
		return intToString(i + 1);
	}

	private static String viewGetProbeIdent(String probeIdent) {
		return probeIdent;
	}

	private static String viewGetLaborIdentRow(int i) {
		return Integer.toString(i + 1);
	}

	private static String viewGetLaborIdentColumn(int i) {
		return intToString(i +1 );
	}

	private static int modelGetLaborIdentRow(final String string)
			throws InvalidSettingsException {
		final InvalidSettingsException xx = new InvalidSettingsException(
				"Row of labor identifier invalid for value " + string);
		int i = -1;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw xx;
		}
		if (i > 0)
			return i - 1;
		throw xx;
	}

	private static int modelGetValuesStartRow(String string)
			throws InvalidSettingsException {
		final InvalidSettingsException xx = new InvalidSettingsException(
				"Row of values start invalid for value " + string);
		int i = -1;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw xx;
		}
		if (i > 0)
			return i - 1;
		throw xx;
	}

	private static int modelGetValuesEndRow(String string)
			throws InvalidSettingsException {
		final InvalidSettingsException xx = new InvalidSettingsException(
				"Row values end invalid for value " + string);
		int i = -1;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw xx;
		}
		if (i > 0)
			return i - 1;
		throw xx;
	}

	private static int modelGetSheetNo(String string)
			throws InvalidSettingsException {
		final InvalidSettingsException xx = new InvalidSettingsException(
				"sheet no invalid for value " + string);
		int i = -1;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw xx;
		}
		if (i > 0)
			return i - 1;
		throw xx;
	}

	private static int modelGetProbeIdentRow(String string)
			throws InvalidSettingsException {
		final InvalidSettingsException xx = new InvalidSettingsException(
				"Row of probe identifier invalid for value " + string);
		int i = -1;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw xx;
		}
		if (i > 0)
			return i - 1;
		throw xx;
	}

	private static String modelGetProbeIdent(String string)
			throws InvalidSettingsException {
		final InvalidSettingsException xx = new InvalidSettingsException(
				"probe identifier invalid for value " + string);
		if (string == null || string.length() == 0)
			throw xx;
		return string;
	}

	private static int modelGetLaborIdentColumn(String laborIdentColumn)
			throws InvalidSettingsException {
		final int i = stringToInt(laborIdentColumn);
		if (i > 0)
			return i - 1;
		throw new InvalidSettingsException(
				"Column of labor identifier invalid for value "
						+ laborIdentColumn);
	}

	private static int modelGetValuesStartColumn(String string)
			throws InvalidSettingsException {
		final int i = stringToInt(string);
		if (i > 0)
			return i - 1;
		throw new InvalidSettingsException(
				"Column of values start invalid for value " + string);
	}

	private static int modelGetValuesEndColumn(String string)
			throws InvalidSettingsException {
		final int i = stringToInt(string);
		if (i > 0)
			return i - 1;
		throw new InvalidSettingsException(
				"Column of values end invalid for value " + string);
	}

	private static int modelGetSubstancesColumn(String string)
			throws InvalidSettingsException {
		final int i = stringToInt(string);
		if (i > 0)
			return i - 1;
		throw new InvalidSettingsException(
				"Column of substances invalid for value " + string);
	}

	private static int modelGetProbeIdentColumn(String string)
			throws InvalidSettingsException {
		final int i = stringToInt(string);
		if (i > 0)
			return i - 1;
		throw new InvalidSettingsException(
				"Column of probe identifier invalid for value " + string);
	}

	private static int stringToInt(String string)
			throws InvalidSettingsException {
		int i = -1;
		char c = 'a';
		if (string.length() < 1) {
			final InvalidSettingsException e = new InvalidSettingsException(
					"unexpected string length: " + string);
			LOGGER.debug(e.getLocalizedMessage(), e);
			throw e;
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

	private static String intToString(int i) {
		final String string = Character.toString((char) (i + 64));
		LOGGER.debug(new StringBuilder()
				.append("converting integer to string:").append(
						Preferences.NEW_LINE).append("integer=").append(i).append(", ")
				.append("string=").append(string));
		return string;
	}

	private static boolean isValidChar(int i) {
		LOGGER.debug("checking char validity for integer " + i);
		if (i > 0 && i < 27)
			return true;
		else
			return false;
	}
}
