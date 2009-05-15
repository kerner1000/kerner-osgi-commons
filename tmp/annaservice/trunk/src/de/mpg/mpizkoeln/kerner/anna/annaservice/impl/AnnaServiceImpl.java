package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

public class AnnaServiceImpl implements AnnaService {
    
    private class CheckReqThread implements Callable<Boolean> {
        
        private final AbstractStep step;
        private final DataObject data;
        
        public CheckReqThread(AbstractStep step, DataObject data) {
            this.step = step;
            this.data = data;
        }

        public Boolean call() throws Exception {
            boolean stepIsReady = step.checkRequirements(data);
            LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "requirements for step are satisfied: " + stepIsReady, null);
            stepsReady.put(step, stepIsReady);
            return null;
        }
        
    }
    
    private static ToOSGiLogServiceLogger LOGGER = null;
    private final Collection<AbstractStep> steps = new ArrayList<AbstractStep>();
    private final ConcurrentMap<AbstractStep, Boolean> stepsReady = new ConcurrentHashMap<AbstractStep, Boolean>();
    private final DataObject data = new DataObject();

    public synchronized void registerStep(AbstractStep step) {
        steps.add(step);
        LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "registered step " + step, null);
        
    }
    
    protected void activate(ComponentContext componentContext) {
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
    }
    
    protected void deactivate(ComponentContext componentContext) {
        LOGGER.disable(componentContext.getBundleContext());
    }

}
