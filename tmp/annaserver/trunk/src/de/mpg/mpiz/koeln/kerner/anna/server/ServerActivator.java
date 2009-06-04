package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;

public class ServerActivator implements BundleActivator {
	
	public static LogDispatcher LOGGER = null;
	
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		try{
		System.err.println(this + " activated");
		LOGGER = new LogDispatcher(context);
		Server service = new ServerImpl();
		context.registerService(Server.class.getName(), service, new Hashtable());
		LOGGER.debug(this, "Server registered");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}

}
