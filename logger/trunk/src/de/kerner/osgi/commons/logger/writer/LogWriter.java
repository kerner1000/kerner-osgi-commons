package de.kerner.osgi.commons.logger.writer;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LogWriter implements LogListener {

	private final static String WORKING_DIR = System.getProperty("user.dir");
	private static final String LOG_PROPERTIES = WORKING_DIR
			+ "/configuration/log.properties";
	private static Logger LOGGER = LoggerFactory.getLogger(LogWriter.class);

	public void logged(LogEntry entry) {
		String message = entry.getMessage();
		Throwable throwable = entry.getException();
		int level = entry.getLevel();
		Bundle bundle = entry.getBundle();
		doTheLog(level, bundle, message, throwable);
	}

	private void doTheLog(int level, Bundle bundle, String message, Throwable t) {
		final String identifier = "[" + bundle.getBundleId() + "]: ";
		switch (level) {
		case LogService.LOG_DEBUG:
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(identifier + message, t);
			break;
		case LogService.LOG_INFO:
			if (LOGGER.isInfoEnabled())
				LOGGER.debug(identifier + message, t);
			break;
		case LogService.LOG_WARNING:
			LOGGER.debug(identifier + message, t);
			break;
		case LogService.LOG_ERROR:
			LOGGER.debug(identifier + message, t);
			break;
		default:
			System.err.println(new StringBuilder()
					.append("Unknown Log message").append(
							System.getProperty("line.separator")).append(level)
					.append(" ").append(bundle).append(": ").append(message)
					.append(" ").append(t));
		}
	}
}
