package de.mpg.mpiz.koeln.kerner.anna.core;

import org.osgi.framework.BundleContext;

import de.mpg.mpiz.koeln.kerner.anna.server.Server;

public class ServerProvider extends AbstractServiceProvider<Server>{

	public ServerProvider(BundleContext context) {
		super(context);
	}

	@Override
	protected Class<Server> getServiceClass() {
		return Server.class;
	}
	
}
