package de.kerner.osgi.commons.logger.writer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;

import de.kerner.osgi.commons.utils.GetServiceAndRun;

public class LogWriterActivator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("starting LogWriter");
		new GetServiceAndRun<LogReaderService>(LogReaderService.class, context) {
			@Override
			public void doSomeThing(LogReaderService s) {
				System.out.println("LogWriter: got service, registering");
				registerListener(s);
			}
		}.run();
		System.out.println("LogWriter ready and running");
	}

	private void registerListener(LogReaderService logReaderService) {
		logReaderService.addLogListener(new LogWriter());
	}

	public void stop(BundleContext context) throws Exception {
		// TODO method stub

	}
}
