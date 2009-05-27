package de.mpg.mpizkoeln.kerner.anna.annaservice.impl;

import java.util.concurrent.Callable;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBean;

class StepController implements Callable<Void> {

    private final AbstractStep step;
    private final DataBean data;

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
        
        if(step.getEnvironment().equals(AbstractStep.Environment.LOCAL)){
        	// call "call()" directly, so that this method will run in current thread.
        	new LocalStepExecutor(step, data).call();
        } else if (step.getEnvironment().equals(AbstractStep.Environment.LSF)) {
        	// call "call()" directly, so that this method will run in current thread.
        	new RemoteStepExecutor(step, data).call();
        } else 
        	throw new RuntimeException("Cannot be");
        
        System.out.println(data.getValidatedFASTASeqs());
       
        AnnaServiceImpl.LOGGER.debug("Step " + step + " finished, notifying others");
        Thread.currentThread().notifyAll();       
        return null;
    }
   
}
