package de.kerner.osgi.commons.logger.dispatcher;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class LogDispatcherImpl implements LogDispatcher {

	private static enum LEVEL {
		INFO, DEBUG, WARN, ERROR
	}

	private final static String THROWER_PREFIX = " ";
	private final static String THROWER_POSTFIX = " ";
	private ServiceTracker tracker = null;

	public LogDispatcherImpl(BundleContext context) {
		if (trackerAssigned(context)) {
			tracker.open();
		} else {
			System.err.println("could not get ServiceTracker for "
					+ LogService.class.getName());
		}
	}

	private boolean trackerAssigned(BundleContext context) {
		// everything good, we do not need to do anything
		if (tracker != null)
			return true;

		tracker = new ServiceTracker(context, LogService.class.getName(), null);
		if (tracker == null)
			return false;
		return true;
	}
	
	public void info(Object cause, Object message) {
		info(cause,message,null);	
	}

	public void info(Object cause, Object message, Throwable t) {
		log(LEVEL.INFO, THROWER_PREFIX + cause + THROWER_POSTFIX + message, t);
	}

	public void debug(Object cause, Object message) {
		debug(cause, message, null);
	}

	public void debug(Object cause, Object message, Throwable t) {
		log(LEVEL.DEBUG, THROWER_PREFIX + cause + THROWER_POSTFIX + message, t);
	}
	
	public void warn(Object cause, Object message) {
		warn(cause,message,null);
		
	}

	public void warn(Object cause, Object message, Throwable t) {
		log(LEVEL.WARN, THROWER_PREFIX + cause + THROWER_POSTFIX + message, t);
	}

	public void error(Object cause, Object message) {
		error(cause, message, null);
	}

	public void error(Object cause, Object message, Throwable t) {
		log(LEVEL.ERROR, THROWER_PREFIX + cause + THROWER_POSTFIX + message,
				t);
	}

	private void log(LEVEL level, Object o, Throwable t) {
		final String cannotLogString = "could not deliver log message:\n\t" + o;

		LogService logservice = (LogService) tracker.getService();
		if (logservice == null) {
			cannotLog(cannotLogString);
			return;
		}
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
	}

	private void cannotLog(String message) {
		System.err.println(message);
	}

	

	
}
