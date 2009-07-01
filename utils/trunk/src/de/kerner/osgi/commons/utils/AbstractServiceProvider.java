package de.kerner.osgi.commons.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
		tracker.open();
		final S s = getServiceClass().cast(tracker.getService());
		return s;
	}
}
