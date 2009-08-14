package de.fh.giessen.ringversuch.common;

/**
 * 
 * <p>Holds application wide constants</p>
 * <p>Message strings are all info or error messages. No debugging strings.</p>
 * 
 * @author Alexander Kerner
 * @lastVisist 2009-08-14
 * 
 */
public class Preferences {

	private Preferences() {
	};

	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String WORKING_DIR = System.getProperty("user.dir");
	public static final String LOG_PROPERTIES = WORKING_DIR + "/log.properties";
	public static final String SETTINGS_FILE = WORKING_DIR + "/settings.ini";

	public static final boolean NATIVE_LAF = true;
	public static final String NAME = "Ringversuch";
	public static final String VERSION = "v.4.0.0";
	
	public static class Controller {
		public static final String SETTINGS_LOADED_GOOD = "settings loaded";
		public static final String SETTINGS_LOADED_BAD = "could not load settings";
		public static final String SETTINGS_SAVED_GOOD = "settings successfully saved";
		public static final String SETTINGS_SAVED_BAD = "could not save settings";
		public static final String SETTINGS_SET_GOOD = "settings successfully set";
		public static final String SETTINGS_SET_BAD = "could not set settings";
		public static final String FILES_LOADED_GOOD = "files loaded";
		public static final String DETECT_GOOD = "detecting settings";
		public static final String DETECT_BAD = "could not detect settings";
		public static final String CANCELED = "canceled";
		public static final String FAILED = "failed";
	}
}
