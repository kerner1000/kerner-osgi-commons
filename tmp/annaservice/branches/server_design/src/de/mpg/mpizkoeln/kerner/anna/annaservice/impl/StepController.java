package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

class StepController implements Callable<Void> {

    private final AbstractStep step;
    private volatile DataObject data;

    StepController(AbstractStep step, DataObject data) {
        this.step = step;
        this.data = data;
    }

    public Void call() throws Exception {
        //System.err.println("Rewuirements: " + step.checkRequirements(data));
        //System.err.println("Interruppted: " + Thread.currentThread().isInterrupted());
        while (!step.checkRequirements(data)) {
            AnnaServiceImpl.LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "Requirements for step " + step + " not satisfied. Putting it to sleep.", null);
            Thread.currentThread().wait();
        }
        if(Thread.currentThread().isInterrupted()){
            Thread.currentThread().notifyAll();
            return null;
        }
        AnnaServiceImpl.LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "Activating step " + step, null);
        // TODO Lock data file
        data = step.run(data);
        // TODO Unlock data file
        Thread.currentThread().notifyAll();
        return null;
    }
}
