package de.mpg.mpiz.koeln.kerner.anna.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.ServerProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;

/**
 * 
 * @ThreadSave
 * 
 */
public abstract class AbstractStep implements BundleActivator {

	public enum State {
		LOOSE, REGISTERED, CHECK_NEED_TO_RUN, WAIT_FOR_REQ, RUNNING, DONE
	}

	// TODO must run in this directory
	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"plugins" + File.separatorChar + "configuration"
					+ File.separatorChar + "step.properties");
	private final Properties properties;
	private State state = State.LOOSE;
	private boolean success = false;
	private boolean skipped = false;

	public AbstractStep() {
		properties = getPropertes();
	}

	protected final synchronized State getState() {
		return state;
	}

	public final synchronized boolean getSuccess() {
		return success;
	}

	public final synchronized void setSuccess(boolean success) {
		this.success = success;
	}

	public final synchronized boolean wasSkipped() {
		return skipped;
	}

	public final synchronized void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}

	protected final synchronized void setState(State state) {
		this.state = state;
	}

	private synchronized Properties getPropertes() {
		final Properties defaultProperties = initDefaults();
		final Properties pro = new Properties(defaultProperties);
		try {
			System.out.println(this + ": loading settings from "
					+ PROPERTIES_FILE);
			final FileInputStream fi = new FileInputStream(PROPERTIES_FILE);
			pro.load(fi);
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println(this + ": could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
		} catch (IOException e) {
			System.out.println(this + ": could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
		}
		return pro;
	}

	/**
	 * should only be called by the OSGi framework
	 */
	public void start(BundleContext context) throws Exception {
		registerToServer(new ServerProvider(context).getService());
		init(context);
	}
	
	protected void init(BundleContext context){
		// Do nothing by default
	}

	private synchronized void registerToServer(Server server) {
		System.out.println(this + ": registering to Server " + server);
		server.registerStep(this);
	}

	/**
	 * should only be called by OSGi framework
	 */
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}

	public synchronized Properties getStepProperties() {
		return properties;
	}

	private Properties initDefaults() {
		Properties pro = new Properties();
		return pro;
	}

	public abstract boolean checkRequirements(DataBean data)
			throws StepExecutionException;

	public abstract boolean needToRun(DataBean data)
			throws StepExecutionException;

	public DataBean run(DataBean data) throws StepExecutionException {
		return run(data, null);
	}

	public abstract DataBean run(DataBean data, StepProcessObserver listener)
			throws StepExecutionException;

}
