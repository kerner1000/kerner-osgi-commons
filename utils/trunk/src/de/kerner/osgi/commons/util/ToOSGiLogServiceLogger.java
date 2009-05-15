package de.kerner.osgi.commons.util;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class ToOSGiLogServiceLogger {

    public static enum LEVEL {
        INFO, DEBUG, WARN, ERROR
    }

    private ServiceTracker logServiceTracker = null;
    private boolean disabled = false;

    public ToOSGiLogServiceLogger(BundleContext context) {
        logServiceTracker =
                new ServiceTracker(context, org.osgi.service.log.LogService.class.getName(), null);
        logServiceTracker.open();
    }

    @Override
    protected void finalize() throws Throwable {
        if (logServiceTracker != null)
            logServiceTracker.close();
        super.finalize();
    }
    
    public void disable(BundleContext context){
        if (logServiceTracker != null)
            logServiceTracker.close();
        this.disabled = true;
    }

    public void log(Object o, LEVEL level, String message, Throwable t) {
        final String cannotLogString = "could not deliver log message:\n " + message + " " + t;
        if(disabled){
            cannotLog(cannotLogString);
            return;
        }
            
        LogService logservice = (LogService) logServiceTracker.getService();
        if (logservice != null) {
            switch (level) {
                case DEBUG:
                    logservice.log(LogService.LOG_DEBUG, message, t);
                    break;
                case ERROR:
                    logservice.log(LogService.LOG_ERROR, message, t);
                case INFO:
                    logservice.log(LogService.LOG_INFO, message, t);
                case WARN:
                    logservice.log(LogService.LOG_WARNING, message, t);
                default:
                    logservice.log(LogService.LOG_INFO, message, t);
                    break;
            }
        } else {
            cannotLog(cannotLogString);
        }
    }
    
    private void cannotLog(String message){
        System.err.println(message);
    }
}
