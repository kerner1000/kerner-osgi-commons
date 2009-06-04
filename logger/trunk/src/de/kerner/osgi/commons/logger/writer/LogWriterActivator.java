package de.kerner.osgi.commons.logger.writer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;

public class LogWriterActivator implements BundleActivator {

	private final static int TIMEOUT = 4000;

	public void start(BundleContext context) throws Exception {
		try {
			System.err.println(this + " activated");
			registerListener(context);
			LogDispatcher logger = new LogDispatcher(context);
			logger.debug(this, "logging ready");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void stop(BundleContext context) throws Exception {
		try {
			System.err.println(this + " deactivated");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void registerListener(BundleContext context) throws Exception {
		ServiceTracker tracker = new ServiceTracker(context,
				LogReaderService.class.getName(), null);
		if (tracker == null)
			throw new RuntimeException("ServiceTracker null");
		tracker.open();
		System.err.println(this + " waiting for " + LogReaderService.class + "...");
		LogReaderService logReaderService = (LogReaderService) tracker
				.waitForService(TIMEOUT);
		System.err.println(this + " got " + LogReaderService.class + ": "
				+ logReaderService);
		if (logReaderService == null)
			throw new RuntimeException("Service null");
		logReaderService.addLogListener(new LogWriter());
		//tracker.close();
		//tracker = null;
		//logReaderService = null;
	}

}
