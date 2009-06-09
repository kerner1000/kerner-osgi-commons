package de.kerner.osgi.commons.logger.writer;

import org.apache.log4j.Logger;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;

class LogWriter implements LogListener {

	private static Logger LOGGER = Logger.getLogger(LogWriter.class);
	
	public void logged(LogEntry entry) {
		String message = entry.getMessage();
        Throwable throwable = entry.getException();
        int level = entry.getLevel();
        Bundle bundle = entry.getBundle();
        doTheLog(level, bundle, message, throwable);
	}
	
	private void doTheLog(int level, Bundle bundle, String message, Throwable t) {
        switch (level) {
        case LogService.LOG_DEBUG:
            LOGGER.debug("[" + bundle.getBundleId() + "]: " + message, t);
            break;
        case LogService.LOG_INFO:
            LOGGER.debug("[" + bundle.getBundleId() + "]: " + message, t);
            break;
        case LogService.LOG_WARNING:
            LOGGER.debug("[" + bundle.getBundleId() + "]: " + message, t);
            break;
        case LogService.LOG_ERROR:
            LOGGER.debug("[" + bundle.getBundleId() + "]: " + message, t);
        default:
            System.err.println("Unknown Log message:\n" + level + " " + bundle + ": " + message + " " + t);
        }
    }

}
