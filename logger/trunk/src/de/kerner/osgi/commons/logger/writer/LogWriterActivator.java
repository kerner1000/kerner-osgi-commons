package de.kerner.osgi.commons.logger.writer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

public class LogWriterActivator implements BundleActivator {

	private ServiceTracker tracker = null;
//	private LogReaderService logReaderService = null;

	public void start(BundleContext context) throws Exception {
		// TODO remove try catch
		try {
			if (trackerAssigned(context)) {
				tracker.open();
				LogReaderService logReaderService = (LogReaderService) tracker
				.getService();
				if(logReaderService == null){
					System.err.println("could not get LogReaderService, no logging available.");
					return;
				}
				registerListener(logReaderService);
			} else {
				System.err.println("could not get ServiceTracker for "
						+ LogReaderService.class.getName());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private void registerListener(LogReaderService logReaderService) {
		logReaderService.addLogListener(new LogWriter());
	}

	private boolean trackerAssigned(BundleContext context) {
		// everything good, we do not need to do anything
		if (tracker != null)
			return true;

		tracker = new ServiceTracker(context,
				LogReaderService.class.getName(), null);
		if (tracker == null)
			return false;
		return true;
	}
	
	public void stop(BundleContext context) throws Exception {
		// TODO method stub
		
	}
}
