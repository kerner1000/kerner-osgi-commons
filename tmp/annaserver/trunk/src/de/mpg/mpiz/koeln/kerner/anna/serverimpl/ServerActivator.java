package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBeanProvider;

class ServerActivator implements BundleActivator {
	
	//public static LogDispatcher LOGGER = null;
	
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		// TODO remove try catch
		try{
		//LOGGER = new LogDispatcher(context);
		Server service = new ServerImpl(new DataBeanProvider(context));
		context.registerService(Server.class.getName(), service, new Hashtable());
		System.out.println("Server activated");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}
}
