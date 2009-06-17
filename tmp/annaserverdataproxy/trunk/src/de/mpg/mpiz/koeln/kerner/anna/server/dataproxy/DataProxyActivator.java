package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl.DataProxyImpl;

public class DataProxyActivator implements BundleActivator {

	// public static LogDispatcher LOGGER = null;

	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		// TODO remove try catch
		try {
			// LOGGER = new LogDispatcher(context);
			DataProxy service = new DataProxyImpl();
			context.registerService(DataProxy.class.getName(), service,
					new Hashtable());
			System.out.println(this + " activated");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}

}
