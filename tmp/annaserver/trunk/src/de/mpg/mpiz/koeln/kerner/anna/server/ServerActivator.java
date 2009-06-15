package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataProxy;

public class ServerActivator implements BundleActivator {
	
	public static LogDispatcher LOGGER = null;
	private final static int TIMEOUT = 2000;
	private BundleContext context;
	
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		// TODO remove try catch
		try{
			this.context = context;
		LOGGER = new LogDispatcher(context);
		Server service = new ServerImpl(this);
		context.registerService(Server.class.getName(), service, new Hashtable());
		LOGGER.debug(this, "Server registered");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}
	
	DataProxy getDataProxy() throws InterruptedException, DataBeanAccessException {
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

}
