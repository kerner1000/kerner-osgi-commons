package de.mpg.mpiz.koeln.kerner.anna.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.osgi.commons.log.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;

public abstract class AbstractStep implements BundleActivator {

	private final static int TIMEOUT = 4000;
	private static LogDispatcher LOGGER = null;

	public static final void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public final void start(BundleContext context) throws Exception {
		try {
			LOGGER = new LogDispatcher(context);
			ServiceTracker tracker = new ServiceTracker(context, Server.class
					.getName(), null);
			if(tracker == null)
				throw new RuntimeException("ServiceTracker null");
			tracker.open();
			LOGGER.debug(this, "getting Server...");
			Server server = (Server) tracker.waitForService(TIMEOUT);
			if (server != null) {
				LOGGER
						.debug(this, "... got Server " + server
								+ ", registering");
				server.registerStep(this);
			} else {
				LOGGER.error(this, "startup failed, could not get Server");
				throw new RuntimeException("Service null");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public final void stop(BundleContext context) throws Exception {
		try {
		ServiceTracker tracker = new ServiceTracker(context, Server.class
				.getName(), null);
		if(tracker == null)
			throw new RuntimeException("ServiceTracker null");
		tracker.open();
		Server server = (Server) tracker.waitForService(TIMEOUT);
		if(server == null)
			throw new RuntimeException("Service null");
		server.unregisterStep(this);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public abstract boolean checkRequirements(DataBean data);
	
	public abstract void run(DataBean data);

}
