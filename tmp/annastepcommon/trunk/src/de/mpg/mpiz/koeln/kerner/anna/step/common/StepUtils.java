package de.mpg.mpiz.koeln.kerner.anna.step.common;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 *
 */
public class StepUtils {

	private StepUtils() {
	};

	public static void handleStepException(Object cause, Throwable t,
			LogDispatcher logger) throws StepExecutionException {
		if (logger == null) {
			handleStepException(cause, t);
		} else {
			t.printStackTrace();
			logger.error(cause, t.getLocalizedMessage(), t);
			throw new StepExecutionException(t);
		}
	}

	public static void handleStepException(Object cause, Throwable t)
			throws StepExecutionException {
		t.printStackTrace();
		throw new StepExecutionException(t);
	}
}
