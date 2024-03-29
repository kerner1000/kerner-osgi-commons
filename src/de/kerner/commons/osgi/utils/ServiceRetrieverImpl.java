package de.kerner.commons.osgi.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ServiceRetrieverImpl<C> implements ServiceRetriever<C>{
	
	private class MyCallable implements Callable<C>{
		private final ServiceRetriever<C> parent;
		private final long delay;
		public MyCallable(ServiceRetriever<C> parent, long delay) {
		this.parent = parent;
		this.delay = delay;
		}
		public C call() throws Exception {
			C result = null;
			while(result == null){
				Thread.sleep(delay);
				result = parent.getService();
			}
			return result;
		}
	}
	
	private final ServiceTracker tracker;
	private final Class<C> c;
	private final ExecutorService exe = Executors.newSingleThreadExecutor();
	
	public ServiceRetrieverImpl(BundleContext context, Class<C> c) {
		this.c = c;
		//System.err.println(c.getName());
		this.tracker = new ServiceTracker(context, c.getName(), null);
		tracker.open();
	}
	
	public C getService() throws ServiceNotAvailabeException{
		C r = c.cast(tracker.getService());
		if(r == null)
			throw new ServiceNotAvailabeException();
		return r;
	}
	
	@Override
	protected void finalize() throws Throwable {
		exe.shutdown();
		tracker.close();
		super.finalize();
	}

	public Future<C> getServiceDelayed(long delay) throws ServiceNotAvailabeException {
		try {
			return exe.submit(new MyCallable(this, delay));
		} catch (Exception e) {
		throw new ServiceNotAvailabeException(e);	
		}
	}
}
