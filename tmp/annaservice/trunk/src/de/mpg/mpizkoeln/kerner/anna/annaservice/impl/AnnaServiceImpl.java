package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

public class AnnaServiceImpl implements AnnaService {

    static ToOSGiLogServiceLogger LOGGER = null;
    private final DataObject data = new DataObject();
    private final ExecutorService exe = Executors.newCachedThreadPool();
    
    public synchronized void registerStep(AbstractStep step) {
        StepController controller = new StepController(step, data);
        exe.submit(controller);
        LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "registered step " + step, null);
    }

    protected void activate(ComponentContext componentContext) {
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        LOGGER.disable(componentContext.getBundleContext());
    }

}
