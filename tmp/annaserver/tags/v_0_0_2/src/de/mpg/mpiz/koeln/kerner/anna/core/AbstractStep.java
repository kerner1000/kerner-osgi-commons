package de.mpg.mpiz.koeln.kerner.anna.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.osgi.commons.log.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataProxy;

public abstract class AbstractStep implements BundleActivator {

	private final static int TIMEOUT = 2000;
	protected static LogDispatcher LOGGER = null;
	private BundleContext context = null;

	public static final void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public final void start(BundleContext context) throws Exception {
		try {
			this.context = context;
			LOGGER = new LogDispatcher(context);
			registerToServer(getServer(context));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private Server getServer(BundleContext context) throws InterruptedException{
		ServiceTracker tracker = new ServiceTracker(context, Server.class
				.getName(), null);
		if (tracker == null)
			throw new RuntimeException("ServiceTracker null");
		tracker.open();
		LOGGER.debug(this, "getting Server...");
		Server server = (Server) tracker.waitForService(TIMEOUT);
		if (server == null)
			throw new RuntimeException("Service null");
		LOGGER.debug(this, "... got Server " + server);
		return server;
	}
	
	private void registerToServer(Server server){
		LOGGER.debug(this, "registering to Server " + server);
		server.registerStep(this);
	}
	
	public final void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}

	public final DataProxy getDataProxy() throws InterruptedException, DataBeanAccessException {
		ServiceTracker tracker = new ServiceTracker(context, DataProxy.class
				.getName(), null);
		if (tracker == null)
			throw new RuntimeException("ServiceTracker null");
		tracker.open();
		LOGGER.debug(this, "getting DataProxy...");
		DataProxy proxy = (DataProxy) tracker.waitForService(TIMEOUT);
		if (proxy == null)
			throw new RuntimeException("Service null");
		LOGGER.debug(this, "... got DataProxy " + proxy);
		return proxy;
	}

	public abstract boolean checkRequirements(DataProxy data);

	public abstract void run(DataProxy data) throws Exception;

}