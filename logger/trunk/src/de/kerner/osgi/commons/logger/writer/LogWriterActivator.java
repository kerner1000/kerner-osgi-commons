package de.kerner.osgi.commons.logger.writer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

public class LogWriterActivator implements BundleActivator {

	private static final String LOG_PROPERTIES = "/home/pcb/kerner/Dropbox/log.properties";
	private final static int TIMEOUT = 4000;
	private final static Logger LOGGER = Logger
			.getLogger(LogWriterActivator.class);
	private ServiceTracker tracker = null;
	private LogReaderService logReaderService = null;

	public LogWriterActivator() {
		PropertyConfigurator.configure(LOG_PROPERTIES);
	}

	public void start(BundleContext context) throws Exception {
		// TODO remove try catch
		try {
			registerListener(context);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void stop(BundleContext context) throws Exception {
		// TODO remove try catch
		try {
			if (tracker != null) {
				tracker.close();
				tracker = null;
			}

			if (logReaderService != null) {
				logReaderService = null;
			}
			LOGGER.debug("stoped");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private boolean getTracker(BundleContext context) {
		// everything good, we do not need to do anything
		if (tracker != null)
			return true;

		tracker = new ServiceTracker(context,
				org.osgi.service.log.LogReaderService.class.getName(), null);
		if (tracker == null)
			return false;
		return true;
	}

	private void registerListener(BundleContext context) throws Exception {
		if (getTracker(context)) {
			tracker.open();
			if (getService(tracker)) {
				LOGGER.error("got Service, registering listener");
				logReaderService.addLogListener(new LogWriter());
			} else {
				LOGGER.error("could not get "
						+ LogReaderService.class.getName());
			}
		} else {
			LOGGER.error("Could not get ServiceTracker for "
					+ LogReaderService.class.getName());
		}
	}

	private boolean getService(ServiceTracker tracker)
			throws InterruptedException {
		// everything good, we do not need to do anything
		if (logReaderService != null)
			return true;

		LOGGER.debug("waiting for " + LogReaderService.class);
		logReaderService = (LogReaderService) tracker
				.waitForService(TIMEOUT);
		if (logReaderService == null)
			return false;
		return true;
	}
}
