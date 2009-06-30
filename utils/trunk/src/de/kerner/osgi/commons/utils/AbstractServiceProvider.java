package de.kerner.osgi.commons.utils;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractServiceProvider<S> {

	protected final BundleContext context;

	public AbstractServiceProvider(BundleContext context) {
		this.context = context;
	}

	protected abstract Class<S> getServiceClass();

	public S getService() {
		final ServiceTracker tracker = new ServiceTracker(context,
				getServiceClass().getName(), null);
		if (tracker == null)
			throw new RuntimeException("ServiceTracker null");
		tracker.open();
		final S proxy = getServiceClass().cast(tracker.getService());
		if (proxy == null)
			throw new RuntimeException("Service null");
		return proxy;
	}

}
