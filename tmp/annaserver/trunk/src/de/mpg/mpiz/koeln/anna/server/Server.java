package de.mpg.mpiz.koeln.anna.server;

import java.util.Properties;

import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractStep;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.serverimpl.StepStateObserver;

public interface Server {

	final static String PROPERTIES_KEY_PREFIX = "anna.server.";
	public final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "workingdir";

	public void registerStep(AbstractStep step);

	public void unregisterStep(AbstractStep step);

	public Properties getServerProperties();

	public StepStateObserver getStepStateObserver();
	
	public AbstractServiceProvider<DataProxy> getDataProxyProvider();

}
