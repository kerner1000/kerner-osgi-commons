package de.mpg.mpiz.koeln.anna.serverimpl;

import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;

class DataProxyProvider extends AbstractServiceProvider<DataProxy>{

	DataProxyProvider(BundleContext context) {
		super(context);
	}

	@Override
	protected Class<DataProxy> getServiceClass() {
		return DataProxy.class;
	}
}
