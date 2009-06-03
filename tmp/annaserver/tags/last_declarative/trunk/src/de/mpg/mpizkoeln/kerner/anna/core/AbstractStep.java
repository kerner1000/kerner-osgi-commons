package de.mpg.mpizkoeln.kerner.anna.core;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.log.util.LogDispatcher;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;

public abstract class AbstractStep {
	
	public enum Environment {
		LOCAL, LSF
	}

	private final static String ENV_KEY ="run.env";
	private final static String ENV_VALUE_LSF ="lsf";
	private final static String ENV_VALUE_LOCAL ="local";
	public static LogDispatcher LOGGER = null;
    private AnnaService annaService = null;
    protected ComponentContext componentContext = null;

    public final static void main(String[] args) {

    }
    
    public Environment getEnvironment(){
    	String s = (String) componentContext.getProperties().get(ENV_KEY);
    	if(s.equalsIgnoreCase(ENV_VALUE_LOCAL))
    		return Environment.LOCAL;
    	else if (s.equalsIgnoreCase(ENV_VALUE_LSF))
    		return Environment.LSF;
    	else
    		throw new RuntimeException("Dont know Environment " + s + " for step " + this);
    }

    protected void setAnnaService(AnnaService annaService) {
        this.annaService = annaService;
    }

    protected void unsetAnnaService(AnnaService annaService) {
        this.annaService = null;
    }

    protected void activate(ComponentContext componentContext) {
        this.componentContext = componentContext;
        LOGGER = new LogDispatcher(componentContext.getBundleContext());
        LOGGER.debug(this, "Step has been activated. Going to register to Service.");
        register(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        LOGGER.debug(this, "Step has been deactivated. Going to unregister from Service.");
        unregister(componentContext.getBundleContext());
        LOGGER.disable(componentContext.getBundleContext());
        this.componentContext = null;
    }

    private void unregister(BundleContext bundleContext) {
    	if (annaService == null) {
            LOGGER.debug(this, "service not set");
        } else {
            annaService.unregisterStep(this);
        }
	}

	private void register(BundleContext context) {
        if (annaService == null) {
            LOGGER.debug(this, "service not set");
        } else {
            annaService.registerStep(this);
        }
    }
    
    public abstract boolean checkRequirements();

    public abstract void run() throws Exception;
}