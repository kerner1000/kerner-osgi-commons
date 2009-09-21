package de.kerner.osgi.commons.logger.writer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;

import de.kerner.osgi.commons.utils.ServiceRetriever;
import de.kerner.osgi.commons.utils.ServiceRetrieverImpl;

public class LogWriterActivator implements BundleActivator {

	private volatile ServiceRetriever<LogReaderService> retriever;

	public void start(BundleContext context) throws Exception {
		this.retriever = new ServiceRetrieverImpl<LogReaderService>(context,
				LogReaderService.class);
		retriever.getService().addLogListener(new LogWriter());

	}

	public void stop(BundleContext context) throws Exception {
		// maaappp
	}
}
