package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

public class AnnaServiceImpl implements AnnaService {
    
    private static ToOSGiLogServiceLogger LOGGER = null;
    private final Collection<AbstractStep> steps = new ArrayList<AbstractStep>();
    private final Map<AbstractStep, Boolean> stepsReady = new HashMap<AbstractStep, Boolean>();

    public void registerStep(AbstractStep step) {
        steps.add(step);
        LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "registered step " + step, null);
        
        boolean stepIsReady = step.checkRequirements(new DataObject());
        LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "requirements for step are satisfied: " + stepIsReady, null);
        
        stepsReady.put(step, stepIsReady);
        
    }
    
    protected void activate(ComponentContext componentContext) {
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
    }
    
    protected void deactivate(ComponentContext componentContext) {
        LOGGER.disable(componentContext.getBundleContext());
    }

}
