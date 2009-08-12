package de.kerner.osgi.commons.logger.dispatcher;
import java.io.Serializable;

/**
 * 
 * @ThreadSave (stateless)
 *
 */
public class ConsoleLogger implements LogDispatcher, Serializable {
	
	private static final long serialVersionUID = -3179084959071600509L;
	private final static String THROWER_PREFIX = " ";
	private final static String THROWER_POSTFIX = " ";

	public void debug(Object cause, Object message) {
		dropToErr(createString("DEBUG", message, null));
	}

	public void debug(Object cause, Object message, Throwable t) {
		dropToErr(createString("DEBUG", message, t));
	}

	public void error(Object cause, Object message) {
		dropToErr(createString("ERROR", message, null));
	}

	public void error(Object cause, Object message, Throwable t) {
		dropToErr(createString("ERROR", message, t));
	}

	public void info(Object cause, Object message) {
		dropToOut(createString("INFO", message, null));
	}

	public void info(Object cause, Object message, Throwable t) {
		dropToOut(createString("INFO", message, t));
	}

	public void warn(Object cause, Object message) {
		dropToOut(createString("WARN", message, null));
	}

	public void warn(Object cause, Object message, Throwable t) {
		dropToOut(createString("WARN", message, t));
	}
	
	private synchronized void dropToOut(String string) {
		System.out.println(string);		
	}
	
	private synchronized void dropToErr(String string) {
		System.err.println(string);	
	}
	
	private String createString(Object level, Object message, Throwable t){
		final StringBuilder sb = new StringBuilder();
		sb.append(level);
		sb.append(THROWER_PREFIX);
		sb.append(THROWER_POSTFIX);
		sb.append(message);
		if(t == null){
			// skip
		} else {
			sb.append(" ");
			sb.append(t);
		}
		return sb.toString();
	}

}