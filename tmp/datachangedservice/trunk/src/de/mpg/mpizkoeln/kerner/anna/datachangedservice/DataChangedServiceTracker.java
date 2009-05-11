package de.mpg.mpizkoeln.kerner.anna.datachangedservice;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class DataChangedServiceTracker extends ServiceTracker {
    
    public DataChangedServiceTracker(BundleContext bundleContext) {
        super(bundleContext, DataChangedService.class.getName(), null);
    }

}
