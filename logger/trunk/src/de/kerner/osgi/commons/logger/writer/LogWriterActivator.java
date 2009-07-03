package de.kerner.osgi.commons.logger.writer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;

import de.kerner.osgi.commons.utils.GetServiceAndRun;

public class LogWriterActivator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		new GetServiceAndRun<LogReaderService>(LogReaderService.class, context) {
			@Override
			public void doSomeThing(LogReaderService s) {
				registerListener(s);
			}
		};
	}

	private void registerListener(LogReaderService logReaderService) {
		logReaderService.addLogListener(new LogWriter());
	}

	public void stop(BundleContext context) throws Exception {
		// TODO method stub

	}
}
