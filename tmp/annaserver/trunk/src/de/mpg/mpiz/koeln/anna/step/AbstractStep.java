package de.mpg.mpiz.koeln.anna.step;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.kerner.osgi.commons.utils.GetServiceAndRun;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

/**
 * @ThredSave
 * @cleaned 2009-07-30
 * @author Alexander Kerner
 * 
 */
public abstract class AbstractStep implements BundleActivator, Step {

	public enum State {
		LOOSE, REGISTERED, CHECK_NEED_TO_RUN, WAIT_FOR_REQ, RUNNING, DONE,
		ERROR
	}

	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration" + File.separatorChar + "step.properties");
	private Properties properties;
	private boolean skipped = false;
	protected volatile LogDispatcher logger = new ConsoleLogger();
	private State state = State.LOOSE;

	public AbstractStep() {
		
	}
	
	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @return
	 * @throws StepExecutionException
	 */
	public boolean run(DataProxy data) throws StepExecutionException {
		return run(data, null);
	}

	public final synchronized State getState() {
		return state;
	}

	public final synchronized boolean wasSkipped() {
		return skipped;
	}

	public final synchronized void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}

	public final synchronized void setState(State state) {
		this.state = state;
	}

	/**
	 * should only be called by the OSGi framework
	 */
	public void start(final BundleContext context) throws Exception {
		logger.debug(this, "starting step " + this);
		try {
			new GetServiceAndRun<Server>(Server.class, context) {
				@Override
				public void doSomeThing(Server s) throws Exception {
					init(context);
					registerToServer(s);
				}
			}.run();
		} catch (Exception e) {
			logger.error(this, "could not start step " + this, e);
			Server s = new AbstractServiceProvider<Server>(context) {
				@Override
				protected Class<Server> getServiceClass() {
					return Server.class;
				}
			}.getService();
			AbstractStep as = new DummyStep(this.toString());
			as.setState(AbstractStep.State.ERROR);
			s.registerStep(as);
		}
	}

	/**
	 * should only be called by OSGi framework
	 */
	public void stop(BundleContext context) throws Exception {
		logger.debug(this, "stopping step " + this);
		// TODO Auto-generated method stub
	}

	public synchronized Properties getStepProperties() {
		return properties;
	}

	protected void init(BundleContext context)
			throws StepExecutionException {
		this.logger = new LogDispatcherImpl(context);
		properties = getPropertes();
	}

	private synchronized void registerToServer(Server server) {
		server.registerStep(this);
	}

	private Properties initDefaults() {
		Properties pro = new Properties();
		return pro;
	}

	private Properties getPropertes() {
		final Properties defaultProperties = initDefaults();
		final Properties pro = new Properties(defaultProperties);
		try {
			logger.info(this, "loading settings from " + PROPERTIES_FILE);
			final FileInputStream fi = new FileInputStream(PROPERTIES_FILE);
			pro.load(fi);
			fi.close();
		} catch (Exception e) {
			logger.error(this, "could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
		}
		return pro;
	}
}