package de.kerner.osgi.commons.util;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class ToOSGiLogServiceLogger {

    private static enum LEVEL {
        INFO, DEBUG, WARN, ERROR
    }
    
    private final static String THROWER_PREFIX = "from: ";
    private final static String THROWER_POSTFIX = " ";
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

    public void debug(Object cause, Object o){
    	log(LEVEL.DEBUG, THROWER_PREFIX + cause.getClass().getSimpleName() + THROWER_POSTFIX + o, null);
    }
    
    public void debug(Object cause, Object o, Throwable t){
    	log(LEVEL.DEBUG, THROWER_PREFIX + cause + THROWER_POSTFIX + o, t);
    }
    
    private void log(LEVEL level, Object o, Throwable t) {
        final String cannotLogString = "could not deliver log message:\n " + o;
        if(disabled){
            cannotLog(cannotLogString);
            return;
        }
        LogService logservice = (LogService) logServiceTracker.getService();
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
    }
    
    private void cannotLog(String message){
        System.err.println(message);
    }
}
