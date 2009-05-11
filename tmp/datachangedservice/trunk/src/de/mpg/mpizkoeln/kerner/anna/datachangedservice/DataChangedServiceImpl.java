package de.mpg.mpizkoeln.kerner.anna.datachangedservice;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;

public class DataChangedServiceImpl implements DataChangedService {
    
    private EventAdmin eventAdmin;
    private static ToOSGiLogServiceLogger LOG;
    
    public void setEventAdmin(EventAdmin eventAdmin){
        this.eventAdmin = eventAdmin;
    }
    
    public void unsetEventAdmin(EventAdmin eventAdmin){
        this.eventAdmin = null;
    }
    
    protected void activate(ComponentContext componentContext){
        LOG = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
        LOG.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, this.getClass().getSimpleName() + " activated.", null);
        //dataChanged();
    }

    protected void deactivate(ComponentContext componentContext){
        LOG.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, this.getClass().getSimpleName() + " deactivated.", null);
        LOG.disable(componentContext.getBundleContext());
    }

    public void dataChanged() {
        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put("hans", "peter");
        Event event = new Event(this.getClass().getName().replace(".", "/"), properties);
        eventAdmin.postEvent(event);
        //System.err.println(this.getClass().getName());
    }
}
