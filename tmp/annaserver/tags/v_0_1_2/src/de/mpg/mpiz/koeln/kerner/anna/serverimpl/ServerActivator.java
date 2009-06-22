package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;

public class ServerActivator implements BundleActivator {

	public static LogDispatcher LOGGER = null;

	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		LOGGER = new LogDispatcherImpl(context);
		Server service = new ServerImpl();
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
