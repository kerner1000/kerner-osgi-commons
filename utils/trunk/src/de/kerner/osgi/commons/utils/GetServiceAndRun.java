package de.kerner.osgi.commons.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.framework.BundleContext;

public abstract class GetServiceAndRun<S> {

	private final Class<S> c;
	private final BundleContext context;
	private final ExecutorService exe = Executors.newSingleThreadExecutor();

	public GetServiceAndRun(Class<S> c, BundleContext context) {
		this.c = c;
		this.context = context;
	}

	public synchronized void run() throws Exception {
		exe.submit(new Callable<Boolean>() {
			public Boolean call() {
				AbstractServiceProvider<S> p = new AbstractServiceProvider<S>(
						context) {
					@Override
					protected Class<S> getServiceClass() {
						return c;
					}
				};
				try {
					final S s = p.getServiceForSure();
					doSomeThing(s);
				} catch (Exception e) {
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		});
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}

	public abstract void doSomeThing(S s) throws Exception;

}
