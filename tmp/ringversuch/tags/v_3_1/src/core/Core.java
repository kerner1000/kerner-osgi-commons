package core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Core {
	
	private final static char REPLACEMENT = '_';
	
	/**
	 * 
	 * Attention: Directory seperators will be removed!
	 * <br>
	 * Don't use whole on pathnames.
	 * 
	 */
	static String formatToValidString(String fileName){
		char[] chars = fileName.toCharArray();
		StringBuilder sb = new StringBuilder();
		Pattern p = Pattern.compile("[a-zA-Z0-9_-]");
		Matcher m;
		for(char c : chars){
			m = p.matcher(new String(Character.toString(c)));
			if(m.matches()){
				sb.append(c);
			} else {
				sb.append(REPLACEMENT);
			}
		}
		return sb.toString();
	}
}
