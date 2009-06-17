package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanProvider;

public class ServerImpl implements Server {

	private final static String PROPERTIES_KEY_PREFIX = "anna.server.";
	private final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX+"workingdir";
//	private final static String WORKING_DIR_VALUE = "/home/pcb/kerner/Desktop/annaWorkingDir/";
	// TODO must run in this directory
	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"plugins" + File.separatorChar + "configuration"
					+ File.separatorChar + "server.properties");
	private final Properties properties;
	private final static int NUM_THREADS = 5;
	private final ExecutorService exe = Executors
			.newFixedThreadPool(NUM_THREADS);
	private final DataBeanProvider provider;

	ServerImpl(DataBeanProvider provider) {
		this.provider = provider;
		properties = getPropertes();
		System.out.println(this + ": loaded properties: " + properties);
		final File workingDir = new File(properties.getProperty(WORKING_DIR_KEY));
		if(checkWorkingDir(workingDir)){
			//
		}
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
//		pro.setProperty(WORKING_DIR_KEY, WORKING_DIR_VALUE);
		return pro;
	}

	public synchronized void registerStep(AbstractStep step) {
		System.out.println(this + ": registering step " + step);
		StepController controller = new StepController(step, provider);
		exe.submit(controller);
		System.out.println(this + ": registered step " + step);
	}

	public synchronized void unregisterStep(AbstractStep step) {
		System.out.println(this + ": unregistering step " + step);
		// TODO method stub

	}

	public Properties getServerProperties() {
		return new Properties(properties);
	}

}
