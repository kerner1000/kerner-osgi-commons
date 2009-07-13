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
}
