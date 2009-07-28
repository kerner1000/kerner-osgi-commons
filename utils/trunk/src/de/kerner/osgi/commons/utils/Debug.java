package de.kerner.osgi.commons.utils;

class Debug {
	
	static String MESSAGE_PREFIX = " ";
	static String MESSAGE_POSTFIX = "";
	boolean debug = true;
	boolean info = true;
	boolean error = true;
	
	void debug(Object source, Object message){
		if(debug)
			System.out.println(source + MESSAGE_PREFIX + message + MESSAGE_POSTFIX);
	}

}
