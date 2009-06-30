package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.DataProxy;

/**
 * 
 * @ThreadSave (everything guarded by this)
 * 
 */
public class ServerImpl implements Server {

	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"plugins" + File.separatorChar + "configuration"
					+ File.separatorChar + "server.properties");
	private final Properties properties;
	private final ExecutorService exe = Executors.newCachedThreadPool();
	private final StepStateObserver monitor;
	private final AbstractServiceProvider<DataProxy> dataProxyProvder;
	private final LogDispatcher logger;

	ServerImpl(final AbstractServiceProvider<DataProxy> provider,
			final LogDispatcher logger) {
		this.monitor = new StepStateObserverImpl();
		this.dataProxyProvder = provider;
		if (logger != null)
			this.logger = logger;
		else
			this.logger = null;
		properties = getPropertes();
		System.out.println(this + ": loaded properties: " + properties);
		final File workingDir = new File(properties
				.getProperty(WORKING_DIR_KEY));
		if (checkWorkingDir(workingDir)) {
			//
		}
	}

	ServerImpl(AbstractServiceProvider<DataProxy> provider) {
		this.monitor = new StepStateObserverImpl();
		this.dataProxyProvder = provider;
		this.logger = null;
		properties = getPropertes();
		System.out.println(this + ": loaded properties: " + properties);
		final File workingDir = new File(properties
				.getProperty(WORKING_DIR_KEY));
		if (checkWorkingDir(workingDir)) {
			//
		}
	}

	public synchronized void registerStep(AbstractStep step) {
		// TODO remove try catch
		try {
			monitor.stepRegistered(step);
			StepController controller = new StepController(step, this);
			exe.submit(controller);
			System.out.println(this + ": registered step " + step);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public synchronized void unregisterStep(AbstractStep step) {
		System.out.println(this + ": unregistering step " + step);
		// TODO method stub

	}

	public synchronized StepStateObserver getStepStateObserver() {
		return monitor;
	}

	public synchronized Properties getServerProperties() {
		return new Properties(properties);
	}
	
	public synchronized AbstractServiceProvider<DataProxy> getDataProxyProvider() {
		return dataProxyProvder;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	private boolean checkWorkingDir(final File workingDir) {
		if (!workingDir.exists()) {
			System.out.println(this + ": " + workingDir
					+ " does not exist, creating");
			final boolean b = workingDir.mkdirs();
			return b;
		}
		return workingDir.canWrite();
	}
	
	private Properties getPropertes() {
		final Properties defaultProperties = initDefaults();
		final Properties pro = new Properties(defaultProperties);
		try {
			System.out.println(this + ": loading settings from "
					+ PROPERTIES_FILE);
			final FileInputStream fi = new FileInputStream(PROPERTIES_FILE);
			pro.load(fi);
			fi.close();
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

	private Properties initDefaults() {
		Properties pro = new Properties();
		// pro.setProperty(WORKING_DIR_KEY, WORKING_DIR_VALUE);
		return pro;
	}

}
