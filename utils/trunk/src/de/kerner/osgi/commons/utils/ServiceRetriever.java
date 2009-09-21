package de.kerner.osgi.commons.utils;

import java.util.concurrent.Future;

public interface ServiceRetriever<S> {

	S getService() throws ServiceNotAvailabeException;
	
	Future<S> getServiceDelayed(long delay) throws ServiceNotAvailabeException;
}
