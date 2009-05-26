package de.mpg.mpizkoeln.kerner.anna.core;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;

public abstract class AbstractStep {

    protected static ToOSGiLogServiceLogger LOGGER = null;
    private AnnaService annaService = null;
    private ComponentContext componentContext = null;

    public final static void main(String[] args) {

    }

    protected void setAnnaService(AnnaService annaService) {
        this.annaService = annaService;
        // System.err.println(this + "got service, ID:" + annaService);
    }

    protected void unsetAnnaService(AnnaService annaService) {
        this.annaService = null;
    }

    protected void activate(ComponentContext componentContext) {
        this.componentContext = componentContext;
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
        LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG,
                "Step has been activated. Going to register to Service.", null);
        register(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG,
                "Step has been deactivated. Going to unregister from Service.", null);
        // TODO: must unregister this step on stepservice
        LOGGER.disable(componentContext.getBundleContext());
        this.componentContext = null;
    }

    // TODO: maybe leave objects just as they are instead of calling
    // object.toString()
    public Properties getStepProperties() {
        Properties properties = new Properties();
        Dictionary<?, ?> dict = componentContext.getProperties();
        Enumeration<?> e = dict.keys();
        // System.err.println(e);
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            // System.err.println(key);
            Object value = dict.get(key);
            // System.err.println(value);
            properties.setProperty(key.toString(), value.toString());
        }
        return properties;
    }

    private void register(BundleContext context) {
        if (annaService == null) {
            LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.ERROR, "service not set", null);
        } else {
            annaService.registerStep(this);
        }
    }
    
    public abstract boolean checkRequirements(DataObject data);

    public abstract DataObject run(DataObject data) throws Exception;
}
