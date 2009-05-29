package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.annaservice.AnnaService;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;

public class AnnaServiceImpl implements AnnaService {

    public static ToOSGiLogServiceLogger LOGGER = null;
    private final ExecutorService exe = Executors.newCachedThreadPool();
    
    public synchronized void registerStep(AbstractStep step) {
        StepController controller = new StepController(step);
        exe.submit(controller);
        LOGGER.debug(this, "registered step " + step);
    }
    
    public synchronized void unregisterStep(AbstractStep abstractStep) {
		// TODO Auto-generated method stub
	}

    protected void activate(ComponentContext componentContext) {
        LOGGER = new ToOSGiLogServiceLogger(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        LOGGER.disable(componentContext.getBundleContext());
    }

}
