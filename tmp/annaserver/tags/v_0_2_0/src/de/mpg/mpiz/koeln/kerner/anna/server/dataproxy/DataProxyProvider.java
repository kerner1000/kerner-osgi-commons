package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy;

import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl.DataProxyImpl;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl.SerializationStrategySer;

/**
 * 
 * @ThreadSave
 *
 */
public class DataProxyProvider {

	private final DataProxy proxy;

	public DataProxyProvider(Server server) {
//		final SerialisationStrategy strategy = new SerializationStrategyXML();
		final SerialisationStrategy strategy = new SerializationStrategySer();
		proxy = new DataProxyImpl(server, strategy);
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	public DataProxy getDataProxy() {
		return proxy;
	}

}
