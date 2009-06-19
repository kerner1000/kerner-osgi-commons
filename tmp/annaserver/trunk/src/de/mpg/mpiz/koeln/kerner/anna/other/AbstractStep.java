package de.mpg.mpiz.koeln.kerner.anna.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.ServerProvider;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;

/**
 * 
 * @threadsave
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
	private LogDispatcherImpl logger = null;

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

	protected final synchronized void setState(State state) {
		this.state = state;
	}

	private synchronized Properties getPropertes() {
		final Properties defaultProperties = initDefaults();
		final Properties pro = new Properties(defaultProperties);
		try {
			info(this, "loading settings from "
					+ PROPERTIES_FILE);
			final FileInputStream fi = new FileInputStream(PROPERTIES_FILE);
			pro.load(fi);
		} catch (FileNotFoundException e) {
			warn(this ,"could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults", e);
		} catch (IOException e) {
			warn(this, "could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults", e);
		}
		return pro;
	}

	/**
	 * should only be called by the OSGi framework
	 */
	public final void start(BundleContext context) throws Exception {
			this.logger = new LogDispatcherImpl(context);
			registerToServer(new ServerProvider(context).getService());
	}

	private synchronized void registerToServer(Server server) {
		info(this, "registering to Server " + server);
		server.registerStep(this);
	}

	/**
	 * should only be called by OSGi framework
	 */
	public final void stop(BundleContext context) throws Exception {
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
	
	public DataBean run(DataBean data)throws StepExecutionException{
		return run(data, null);
	}
	
	public void info(Object cause, Object message){
		logger.info(cause, message);
	}

	public void info(Object cause, Object message, Throwable t){
		logger.info(cause, message, t);
	}
	
	public void debug(Object cause, Object message){
		logger.debug(cause, message);
	}

	public void debug(Object cause, Object message, Throwable t){
		logger.debug(cause, message, t);
	}
	
	public void error(Object cause, Object message){
		logger.error(cause, message);
	}

	public void warn(Object cause, Object message, Throwable t){
		logger.warn(cause, message, t);
	}

	public void warn(Object cause, Object message){
		logger.warn(cause, message);
	}

	public void error(Object cause, Object message, Throwable t){
		logger.error(cause, message, t);
	}

	public abstract DataBean run(DataBean data, StepProcessObserver listener) throws StepExecutionException;

}
