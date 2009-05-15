package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.concurrent.Callable;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

class StepController implements Callable<Boolean>{

    private final AbstractStep step;
    private final DataObject data;
    
    StepController(AbstractStep step, DataObject data) {
        this.step = step;
        this.data = data;
    }

    public Boolean call() throws Exception {
        System.err.println(step.checkRequirements(data));
        return null;
    }

}
