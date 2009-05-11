package de.mpg.mpizkoeln.kerner.anna.generictester;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.mpg.mpizkoeln.kerner.anna.datachangedservice.DataChangedEvent;
import de.mpg.mpizkoeln.kerner.anna.datachangedservice.DataChangedListener;

public class Activator implements BundleActivator {
    
    private final DataChangedListener listener = new DataChangedListener(){
        public void dataChanged(DataChangedEvent event) {
          System.err.println("Data has changed. " + event);
        }  
    };

    public void start(BundleContext context) throws Exception {
        context.registerService(DataChangedListener.class.getName(),
                listener, null);
    }

    public void stop(BundleContext context) throws Exception {
        
    }

    

}
