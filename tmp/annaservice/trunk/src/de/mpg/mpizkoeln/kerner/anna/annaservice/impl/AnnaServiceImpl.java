package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBean;

public class AnnaServiceImpl implements AnnaService {

    static ToOSGiLogServiceLogger LOGGER = null;
    private final DataBean data = new DataBean();
    private final ExecutorService exe = Executors.newCachedThreadPool();
    
    public synchronized void registerStep(AbstractStep step) {
        StepController controller = new StepController(step, data);
        exe.submit(controller);
        LOGGER.debug("registered step " + step);
    }

    protected void activate(ComponentContext componentContext) {
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        LOGGER.disable(componentContext.getBundleContext());
    }

}
