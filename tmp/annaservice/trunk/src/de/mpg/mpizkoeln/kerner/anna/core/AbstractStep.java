package de.mpg.mpizkoeln.kerner.anna.core;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;

public abstract class AbstractStep {

    protected static ToOSGiLogServiceLogger LOGGER = null;
    private AnnaService annaService = null;
    
    public final static void main(String[] args) {
       
    }
    
    protected void setAnnaService(AnnaService annaService){
        this.annaService = annaService;
        //System.err.println("pfft");
    }
    
    protected void unsetAnnaService(AnnaService annaService){
        this.annaService = null;
    }
    
    protected void activate(ComponentContext componentContext) {
        //System.err.println("pfft");
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
        register(componentContext.getBundleContext());
    }
    
    protected void deactivate(ComponentContext componentContext) {
        
        // TODO: must unregister this step on stepservice
        LOGGER.disable(componentContext.getBundleContext());
    }
    
    private void register(BundleContext context){
       if(annaService == null){
           LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.ERROR, "service not set", null);
       } else {
           annaService.registerStep(this);
       }
    }

    public abstract boolean checkRequirements(DataObject data);

}
