package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.Properties;

import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxyimpl.DataProxyProvider;
import de.mpg.mpiz.koeln.kerner.anna.serverimpl.StepStateMonitor;

public interface Server {

	final static String PROPERTIES_KEY_PREFIX = "anna.server.";
	public final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX+"workingdir";
	public void registerStep(AbstractStep step);
	public void unregisterStep(AbstractStep step);
	public Properties getServerProperties();
	public DataProxyProvider getDataProxyProvider();
	public StepStateMonitor getStepStatemonitor();
	
}
