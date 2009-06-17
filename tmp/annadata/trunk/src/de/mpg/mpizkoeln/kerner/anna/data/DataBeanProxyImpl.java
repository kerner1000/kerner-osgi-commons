package de.mpg.mpizkoeln.kerner.anna.data;

import org.osgi.service.component.ComponentContext;

import de.kerner.osgi.commons.log.util.LogDispatcher;

public class DataBeanProxyImpl implements DataBeanProxy {
	
	private static LogDispatcher LOGGER = null; 
	
	protected void activate(ComponentContext context){
		LOGGER = new LogDispatcher(context.getBundleContext());
		LOGGER.debug(this, "activated.");
	}

	protected void deactivate(ComponentContext context){
		LOGGER.debug(this, "deactivated.");
		LOGGER = null;
	}
}
