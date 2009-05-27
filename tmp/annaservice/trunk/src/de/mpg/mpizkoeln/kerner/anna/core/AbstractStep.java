package de.mpg.mpizkoeln.kerner.anna.core;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;

public abstract class AbstractStep {
	
	public enum Environment {
		LOCAL, LSF
	}

	private final static String ENV_KEY ="run.env";
	private final static String ENV_VALUE_LSF ="lsf";
	private final static String ENV_VALUE_LOCAL ="local";
	public static ToOSGiLogServiceLogger LOGGER = null;
    private AnnaService annaService = null;
    private ComponentContext componentContext = null;

    public final static void main(String[] args) {

    }
    
    public Environment getEnvironment(){
    	String s = getStepProperties().getProperty(ENV_KEY);
    	if(s.equalsIgnoreCase(ENV_VALUE_LOCAL))
    		return Environment.LOCAL;
    	else if (s.equalsIgnoreCase(ENV_VALUE_LSF))
    		return Environment.LSF;
    	else
    		throw new RuntimeException("Dont know Environment " + s + " for step " + this);
    }
    
    // TODO: maybe leave objects just as they are instead of calling
    // object.toString()
    public Properties getStepProperties() {
        Properties properties = new Properties();
        Dictionary<?, ?> dict = componentContext.getProperties();
        Enumeration<?> e = dict.keys();
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            Object value = dict.get(key);
            properties.setProperty(key.toString(), value.toString());
        }
        return properties;
    }

    protected void setAnnaService(AnnaService annaService) {
        this.annaService = annaService;
    }

    protected void unsetAnnaService(AnnaService annaService) {
        this.annaService = null;
    }

    protected void activate(ComponentContext componentContext) {
        this.componentContext = componentContext;
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
        LOGGER.debug("Step has been activated. Going to register to Service.");
        register(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        LOGGER.debug("Step has been deactivated. Going to unregister from Service.");
        // TODO: must unregister this step on stepservice
        LOGGER.disable(componentContext.getBundleContext());
        this.componentContext = null;
    }

    private void register(BundleContext context) {
        if (annaService == null) {
            LOGGER.debug("service not set");
        } else {
            annaService.registerStep(this);
        }
    }
    
    public abstract boolean checkRequirements(DataBean data);

    public abstract void run(DataBean data) throws Exception;
}
