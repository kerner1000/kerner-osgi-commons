package de.kerner.osgi.commons.logger.writer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;

class LogWriter implements LogListener {

	private static final String LOG_PROPERTIES = "/home/pcb/kerner/Dropbox/log.properties";
	private static Logger LOGGER = Logger.getLogger(LogWriter.class);
	
	LogWriter(){
		PropertyConfigurator.configure(LOG_PROPERTIES);
	}
	
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
            LOGGER.debug(identifier + message, t);
            break;
        case LogService.LOG_INFO:
            LOGGER.debug(identifier + message, t);
            break;
        case LogService.LOG_WARNING:
            LOGGER.debug(identifier + message, t);
            break;
        case LogService.LOG_ERROR:
            LOGGER.debug(identifier + message, t);
        default:
            System.err.println("Unknown Log message:\n" + level + " " + bundle + ": " + message + " " + t);
        }
    }

}
