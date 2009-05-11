package de.kerner.osgi.commons.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

public class OSGi2ApacheLogger implements LogListener {
    
    private final static Logger LOGGER = Logger.getLogger(OSGi2ApacheLogger.class);
    private LogReaderService logReaderService;

    public void setLogReaderService(LogReaderService logReaderService) {
        this.logReaderService = logReaderService;
    }

    public void unsetLogReaderService(LogReaderService logReaderService) {
        this.logReaderService = null;
    }

    protected void activate(ComponentContext componentContext) {
        logReaderService.addLogListener(this);
        String s = "Dropbox/log.properties";
        PropertyConfigurator.configure(s);
    }

    protected void deactivate(ComponentContext componentContext) {
        logReaderService.removeLogListener(this);
    }

    public void logged(LogEntry entry) {
        String message = entry.getMessage();
        Throwable throwable = entry.getException();
        int level = entry.getLevel();
        doTheLog(level, message, throwable);        
    }
    
    private void doTheLog(int level, String message, Throwable t) {
        switch (level) {
        case LogService.LOG_DEBUG:
            LOGGER.debug(message, t);
            break;
        case LogService.LOG_INFO:
            LOGGER.info(message, t);
            break;
        case LogService.LOG_WARNING:
            LOGGER.warn(message, t);
            break;
        case LogService.LOG_ERROR:
            LOGGER.error(message, t);
        default:
            System.err.println("Unknown Log message: " + level + " " + message + " " + t);
        }
    }
}
