package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;

public class ServerActivator implements BundleActivator {

	public static LogDispatcher LOGGER = null;
	public static AbstractServiceProvider<DataProxy> provider = null;

	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		provider = new DataProxyProvider(context);
		LOGGER = new LogDispatcherImpl(context);
		Server service = new ServerImpl(provider, LOGGER);
		context.registerService(Server.class.getName(), service,
				new Hashtable());
		LOGGER.debug(this, "activated");
	}

	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
