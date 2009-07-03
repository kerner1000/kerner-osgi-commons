package de.kerner.osgi.commons.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractServiceProvider<S> {

	public final static long DEFAULT_TIMEOUT = 500;
	private final long timeout;
	final ServiceTracker tracker;
	private final ExecutorService exe = Executors.newSingleThreadExecutor();

	public AbstractServiceProvider(BundleContext context) {
		tracker = new ServiceTracker(context, getServiceClass().getName(), null);
		tracker.open();
		timeout = DEFAULT_TIMEOUT;
	}

	public AbstractServiceProvider(BundleContext context, long timeout) {
		tracker = new ServiceTracker(context, getServiceClass().getName(), null);
		tracker.open();
		this.timeout = timeout;
	}

	protected abstract Class<S> getServiceClass();

	public synchronized S getService() {
		return get();
	}

	public synchronized S getServiceForSure()
			throws ServiceNotAvailabeException {
		final S service = get();
		if (service == null)
			throw new ServiceNotAvailabeException("service " + service
					+ " currently not availabe");
		return service;
	}

	public synchronized Future<S> waitForService() {
		return exe.submit(new Callable<S>() {
			public S call() throws Exception {
				while (get() == null) {
					System.out.println("no server found, trying again in "
							+ timeout + " millisecs");
					Thread.sleep(timeout);
				}
				return get();
			}
		});
	}

	public synchronized S waitForServiceAndBlock() throws InterruptedException, ExecutionException{
		return exe.submit(new Callable<S>() {
			public S call() throws Exception {
					while (get() == null) {
						System.out.println("no server found, trying again in "
								+ timeout + " millisecs");
						Thread.sleep(timeout);
					}
					return get();
			}
		}).get();
	}

	private S get() {
		final S s = getServiceClass().cast(tracker.getService());
		return s;
	}
}
