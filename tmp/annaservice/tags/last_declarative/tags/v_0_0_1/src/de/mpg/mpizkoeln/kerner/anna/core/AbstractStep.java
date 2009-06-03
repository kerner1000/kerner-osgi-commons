package de.mpg.mpizkoeln.kerner.anna.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;

public abstract class AbstractStep implements BundleActivator {

    protected static ToOSGiLogServiceLogger LOGGER = null;
    private ServiceTracker tracker = null;
    
    public final static void main(String[] args) {
       
    }
    
    public void start(BundleContext context) throws Exception {
        LOGGER = new ToOSGiLogServiceLogger(context);
        tracker = new ServiceTracker(context, AnnaService.class.getName(), null);
        tracker.open();
        register(context);
    }

    public void stop(BundleContext context) throws Exception {
        tracker.close();
        LOGGER.disable(context);
    }
    
    private void register(BundleContext context){
       AnnaService service =  (AnnaService) tracker.getService();
       if(service == null){
           LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.WARN, "cannot fetch service", null);
       } else {
           LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "got service reference, registering.", null);
           service.registerStep(this);
       }
    }

    public abstract boolean checkRequirements(DataObject data);

}
