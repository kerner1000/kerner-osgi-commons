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
        LOGGER.debug("Step has been activated. Going to register to Service.");
        register(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        LOGGER.debug("Step has been deactivated. Going to unregister from Service.");
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
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            Object value = dict.get(key);
            properties.setProperty(key.toString(), value.toString());
        }
        return properties;
    }

    private void register(BundleContext context) {
        if (annaService == null) {
            LOGGER.debug("service not set");
        } else {
            annaService.registerStep(this);
        }
    }
    
    public abstract boolean checkRequirements(DataBean data);

    public abstract DataBean run(DataBean data) throws Exception;
}
