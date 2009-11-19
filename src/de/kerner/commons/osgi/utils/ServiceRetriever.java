package de.kerner.commons.osgi.utils;

import java.util.concurrent.Future;

public interface ServiceRetriever<S> {

	S getService() throws ServiceNotAvailabeException;
	
	Future<S> getServiceDelayed(long delay) throws ServiceNotAvailabeException;
}
