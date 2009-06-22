package de.mpg.mpiz.koeln.kerner.anna.server;

import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.utils.AbstractServiceProvider;

public class ServerProvider extends AbstractServiceProvider<Server> {

	public ServerProvider(BundleContext context) {
		super(context);
	}

	@Override
	protected Class<Server> getServiceClass() {
		return Server.class;
	}

}
