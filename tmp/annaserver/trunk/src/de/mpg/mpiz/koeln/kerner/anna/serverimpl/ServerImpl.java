package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBeanProvider;

public class ServerImpl implements Server {

	private final static int NUM_THREADS = 5;
	private final ExecutorService exe = Executors
			.newFixedThreadPool(NUM_THREADS);
	private final DataBeanProvider provider;

	ServerImpl(DataBeanProvider provider) {
		this.provider = provider;
	}

	public synchronized void registerStep(AbstractStep step) {
		System.out.println(this + ": registering step " + step);
		StepController controller = new StepController(step, provider);
		exe.submit(controller);
		System.out.println(this + ": registered step " + step);
	}

	public synchronized void unregisterStep(AbstractStep step) {
		System.out.println(this + ": unregistering step " + step);
		// TODO method stub

	}

}
