package de.mpg.mpiz.koeln.kerner.anna.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;

public abstract class AbstractStep implements BundleActivator {

	// TODO must run in this directory
	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"plugins" + File.separatorChar + "configuration"
					+ File.separatorChar + "step.properties");
	// public final static String KEY_ENV = "env";
	// public final static String VALUE_ENV_LOCAL = "env.local";
	// public final static String VALUE_ENV_LSF = "env.lsf";
	private final static int TIMEOUT = 2000;
	// private static LogDispatcher LOGGER = null;
	private final Properties properties;
	// TODO obsolete
	public Bundle bundle;

	public AbstractStep() {
		properties = getPropertes();
		System.out.println(this + ": loaded properties: " + properties);
	}

	private Properties getPropertes() {
		final Properties defaultProperties = initDefaults();
		final Properties pro = new Properties(defaultProperties);
		try {
			System.out.println(this + ": loading settings from "
					+ PROPERTIES_FILE);
			final FileInputStream fi = new FileInputStream(PROPERTIES_FILE);
			pro.load(fi);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(this + ": could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(this + ": could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
		}
		return pro;
	}

	public final void start(BundleContext context) throws Exception {
		// TODO remove try catch
		try {
			this.bundle = context.getBundle();
			// LOGGER = new LogDispatcher(context);
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
		System.out.print(this + ": getting Server...");
		Server server = (Server) tracker.waitForService(TIMEOUT);
		if (server == null)
			throw new RuntimeException("Service null");
		System.out.println(this + ": got Server " + server);
		return server;
	}

	private void registerToServer(Server server) {
		System.out.println(this + ": registering to Server " + server);
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
		// pro.setProperty(KEY_ENV, VALUE_ENV_LOCAL);
		return pro;
	}

	public abstract boolean checkRequirements(DataBean data);

	public abstract DataBean run(DataBean data) throws Exception;

}
