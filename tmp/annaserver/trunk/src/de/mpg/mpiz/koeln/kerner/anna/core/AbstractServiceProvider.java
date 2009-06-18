package de.mpg.mpiz.koeln.kerner.anna.core;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractServiceProvider<S> {

	protected final static int TIMEOUT = 2000;
	protected final BundleContext context;

	public AbstractServiceProvider(BundleContext context) {
		this.context = context;
	}

	protected abstract Class<S> getServiceClass();

	public S getService() throws InterruptedException {
		final ServiceTracker tracker = new ServiceTracker(context,
				getServiceClass().getName(), null);
		if (tracker == null)
			throw new RuntimeException("ServiceTracker null");
		tracker.open();
		System.out.print("getting DataProxy...");
		final S proxy = getServiceClass().cast(tracker.waitForService(TIMEOUT));
		if (proxy == null)
			throw new RuntimeException("Service null");
		System.out.println(this + ": got DataProxy " + proxy);
		return proxy;
	}

}
