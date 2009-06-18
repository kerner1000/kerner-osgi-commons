package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.mpg.mpiz.koeln.kerner.anna.core.DataBeanProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;

public class ServerActivator implements BundleActivator {
	
	//public static LogDispatcher LOGGER = null;
	
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		// TODO remove try catch
		try{
		//LOGGER = new LogDispatcher(context);
		Server service = new ServerImpl(new DataBeanProvider(context));
		context.registerService(Server.class.getName(), service, new Hashtable());
		System.out.println(this + ": activated");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}
}
