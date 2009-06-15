package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;

public class ServerImpl implements Server {
	
	 private final ExecutorService exe = Executors.newFixedThreadPool(5);
	 private final ServerActivator serverActivator;
	
	ServerImpl(ServerActivator serverActivator) {
		this.serverActivator = serverActivator;
	}

	public synchronized void registerStep(AbstractStep step) {
		ServerActivator.LOGGER.debug(this, "registering step " + step);
		StepController controller = new StepController(step, serverActivator);
        exe.submit(controller);
        ServerActivator.LOGGER.debug(this, "registered step " + step);
	}

	public synchronized void unregisterStep(AbstractStep step) {
		ServerActivator.LOGGER.debug(this, "unregistering step " + step);
		// TODO method stub
		
	}

}
