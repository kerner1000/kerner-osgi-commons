package de.kerner.osgi.commons.logger.dispatcher;

public interface LogDispatcher {
	
	public void info(Object cause, Object message);

	public void info(Object cause, Object message, Throwable t);
	
	public void debug(Object cause, Object message);

	public void debug(Object cause, Object message, Throwable t);
	
	public void warn(Object cause, Object message);

	public void warn(Object cause, Object message, Throwable t);

	public void error(Object cause, Object message);

	public void error(Object cause, Object message, Throwable t);

}
