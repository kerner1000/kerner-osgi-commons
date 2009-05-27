package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.concurrent.Callable;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBean;

class StepController implements Callable<Void> {

    private final AbstractStep step;
    private volatile DataBean data;

    StepController(AbstractStep step, DataBean data) {
        this.step = step;
        this.data = data;
    }

    public Void call() throws Exception {
        while (!step.checkRequirements(data)) {
            AnnaServiceImpl.LOGGER.debug("Requirements for step " + step + " not satisfied. Putting it to sleep.");
            Thread.currentThread().wait();
        }
        if(Thread.currentThread().isInterrupted()){
            Thread.currentThread().notifyAll();
            return null;
        }
        AnnaServiceImpl.LOGGER.debug("Activating step " + step);
        // TODO Lock data file
        data = step.run(data);
        // TODO Unlock data file
        Thread.currentThread().notifyAll();
        return null;
    }
}
