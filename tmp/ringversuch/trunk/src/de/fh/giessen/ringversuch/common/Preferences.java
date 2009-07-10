package de.fh.giessen.ringversuch.common;


public class Preferences {
	
	private Preferences(){};
	
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String WORKING_DIR = System.getProperty("user.dir");
	public final static String LOG_PROPERTIES = WORKING_DIR + "/log.properties";
	public static final String SETTINGS_FILE = WORKING_DIR + "/settings.ini";
	
	public static final boolean NATIVE_LAF = true;
	public static final String NAME = "Ringversuch";
	public static final String VERSION = "v.4.0.0";

	public final static String SETTINGS_HEADER = "This is a settings file for \"Ringversuch-v.x.x.jar\"";
	public final static String LABOR_NO_ROW = "labor.no.row";
	public final static String LABOR_NO_COLUMN = "labor.no.column";
	public final static String PROBE_NO_ROW = "probe.no.row";
	public final static String PROBE_NO_COLUMN = "probe.no.column";
	public final static String PROBE_VALUE = "probe.ident";
	public final static String SUBSTANCES_COLUMN = "substances.column";
	public final static String VALUES_START_ROW = "values.start.row";
	public final static String VALUES_START_COLUMN = "values.start.column";
	public final static String VALUES_END_ROW = "values.end.row";
	public final static String VALUES_END_COLUMN = "values.end.column";
	public final static String SHEET_NO = "sheet.no";
	
	
}
