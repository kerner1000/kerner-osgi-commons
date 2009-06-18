package de.mpg.mpiz.koeln.kerner.anna.core;

import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl.DataProxyImpl;

public class DataProxyProvider {
	
	private final DataProxy proxy;
	
	public DataProxyProvider(Server server) {
		proxy = new DataProxyImpl(server);
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	public DataProxy getDataProxy(){
		return proxy;
	}

}
