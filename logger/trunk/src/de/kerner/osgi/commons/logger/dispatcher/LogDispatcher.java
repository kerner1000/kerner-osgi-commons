package de.kerner.osgi.commons.logger.dispatcher;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class LogDispatcher {

	private static enum LEVEL {
		INFO, DEBUG, WARN, ERROR
	}

	private final static String THROWER_PREFIX = " ";
	private final static String THROWER_POSTFIX = " ";
	private final static int TIMEOUT = 1000;
	private ServiceTracker logServiceTracker = null;
	

	public LogDispatcher(BundleContext context) {
		System.err.println(this + " created");
		logServiceTracker = new ServiceTracker(context,
				org.osgi.service.log.LogService.class.getName(), null);
		if(logServiceTracker == null)
			throw new RuntimeException("ServiceTracker null");
		logServiceTracker.open();
		debug(this, "logging ready");
	}

	public void debug(Object cause, Object message) {
		log(LEVEL.DEBUG, THROWER_PREFIX + cause.getClass().getSimpleName()
				+ THROWER_POSTFIX + message, null);
	}

	public void debug(Object cause, Object message, Throwable t) {
		log(LEVEL.DEBUG, THROWER_PREFIX + cause + THROWER_POSTFIX + message, t);
	}
	
	public void error(Object cause, Object message) {
		error(cause, message, null);
	}
	
	public void error(Object cause, Object message, Throwable t) {
		error(LEVEL.ERROR, THROWER_PREFIX + cause + THROWER_POSTFIX + message, t);
	}

	private void log(LEVEL level, Object o, Throwable t) {
		final String cannotLogString = "could not deliver log message:\n " + o;
		try {
			LogService logservice = (LogService) logServiceTracker
					.waitForService(TIMEOUT);
			if (logservice != null) {
				switch (level) {
				case DEBUG:
					logservice.log(LogService.LOG_DEBUG, o.toString(), t);
					break;
				case ERROR:
					logservice.log(LogService.LOG_ERROR, o.toString(), t);
				case INFO:
					logservice.log(LogService.LOG_INFO, o.toString(), t);
				case WARN:
					logservice.log(LogService.LOG_WARNING, o.toString(), t);
				default:
					throw new RuntimeException("cannot be!");
				}
			} else {
				cannotLog(cannotLogString);
			}
		} catch (InterruptedException e) {
			cannotLog(e + " " + cannotLogString);
		}
	}

	private void cannotLog(String message) {
		System.err.println(message);
	}
}
