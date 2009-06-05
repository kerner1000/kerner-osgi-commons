package de.fh.giessen.ringversuch.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Core {
	
	private Core(){}

	private final static char REPLACEMENT = '_';

	/**
	 * <p>
	 * This method will take a string and replace all characters with {@code
	 * REPLACEMENT}, that can cause any problems on actions related to file
	 * system. <br>
	 * These problematical characters are for instance OS dependent, illegal
	 * filename characters or filename separators.
	 * </p>
	 * Attention: Directory separators such as "/" will be removed! <br>
	 * Don't use strings, that represent pathnames.
	 * 
	 * @param string
	 *            String, that is to be reformatted.
	 * @return the new String, containing only chars, that are not
	 *         problematically
	 */
	static String formatToValidString(String string) {
		char[] chars = string.toCharArray();
		StringBuilder sb = new StringBuilder();
		Pattern p = Pattern.compile("[a-zA-Z0-9_-]");
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
}
