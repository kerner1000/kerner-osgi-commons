package de.mpg.mpiz.koeln.kerner.anna.core;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.utils.AnnaUtils;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBean;

public abstract class AbstractStep implements BundleActivator {

	public final static String KEY_ENV = "env";
	public final static String VALUE_ENV_LOCAL = "env.local";
	public final static String VALUE_ENV_LSF = "env.lsf";
	private final static int TIMEOUT = 2000;
	private static LogDispatcher LOGGER = null;
	private final Properties properties;
	// TODO obsolete
	public Bundle bundle;
	
	public AbstractStep() {
		final Properties defaultProperties = initDefaults();
		properties = new Properties(defaultProperties);
	}

	public final void start(BundleContext context) throws Exception {
		try {
			this.bundle = context.getBundle();
			LOGGER = new LogDispatcher(context);
			registerToServer(getServer(context));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private Server getServer(BundleContext context) throws InterruptedException {
		ServiceTracker tracker = new ServiceTracker(context, Server.class
				.getName(), null);
		if (tracker == null)
			throw new RuntimeException("ServiceTracker null");
		tracker.open();
		LOGGER.debug(this, "getting Server...");
		Server server = (Server) tracker.waitForService(TIMEOUT);
		if (server == null)
			throw new RuntimeException("Service null");
		LOGGER.debug(this, "... got Server " + server);
		return server;
	}

	private void registerToServer(Server server) {
		LOGGER.debug(this, "registering to Server " + server);
		server.registerStep(this);
	}

	public final void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}

	public Properties getStepProperties() {
		return properties;
	}

	private Properties initDefaults() {
		Properties pro = new Properties();
		pro.setProperty(KEY_ENV, VALUE_ENV_LOCAL);
		return pro;
	}

	public abstract boolean checkRequirements(DataBean data);

	public abstract void run(DataBean data) throws Exception;

	public static final void main(String[] args) {
		final String path = args[0];
		final File file = new File(path);
		try {
			final DataBean data = AnnaUtils
					.fileToObject(DataBean.class, file);
			System.err.println("!!!--"+data+"---!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
