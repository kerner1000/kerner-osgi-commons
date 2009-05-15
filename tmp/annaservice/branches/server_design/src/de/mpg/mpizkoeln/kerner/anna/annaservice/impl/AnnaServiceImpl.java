package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

public class AnnaServiceImpl implements AnnaService {

    private static ToOSGiLogServiceLogger LOGGER = null;
    private final Collection<StepController> controller = new ArrayList<StepController>();
    private final ExecutorService controllerExecutor = Executors.newCachedThreadPool();
    private final DataObject data = new DataObject();

    public synchronized void registerStep(AbstractStep step) {
        StepController sc = new StepController(step, data);
        controller.add(sc);
        controllerExecutor.submit(sc);
        LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "registered step " + step, null);

    }

    protected void activate(ComponentContext componentContext) {
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        LOGGER.disable(componentContext.getBundleContext());
    }

}
