package de.mpg.mpizkoeln.kerner.anna.generictester;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class Activator implements BundleActivator, EventHandler {

    public void start(BundleContext context) throws Exception {
        
        
    }

    public void stop(BundleContext context) throws Exception {
        
        
    }

    public void handleEvent(Event event) {
       System.err.println("klappt..." + event);
    }

}
