package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;


public class DataBeanProvider {
	
	private final static int TIMEOUT = 2000;
	private final BundleContext context;
	public DataBeanProvider(BundleContext context){
		this.context = context;
	}
	
	public DataProxy getDataProxy() throws InterruptedException, DataBeanAccessException {
		ServiceTracker tracker = new ServiceTracker(context, DataProxy.class
				.getName(), null);
		if (tracker == null)
			throw new RuntimeException("ServiceTracker null");
		tracker.open();
		System.out.println("getting DataProxy...");
		DataProxy proxy = (DataProxy) tracker.waitForService(TIMEOUT);
		if (proxy == null)
			throw new RuntimeException("Service null");
		System.out.println("... got DataProxy " + proxy);
		return proxy;
	}

}
