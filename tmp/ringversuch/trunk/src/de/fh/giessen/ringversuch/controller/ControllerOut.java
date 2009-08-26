package de.fh.giessen.ringversuch.controller;

/**
 * 
 * <p>
 * Handle Events from Model to View.
 * </p>
 * 
 * @lastVisit 2009-08-26
 * @author Alexander Kerner
 * 
 */
public interface ControllerOut {

	/**
	 * <p>
	 * from Model to View
	 * </p>
	 */
	void outgoingPrintMessage(String message, boolean isError);

	/**
	 * <p>
	 * from Model to View
	 * </p>
	 */
	void outgoingShowError(String message);

	/**
	 * <p>
	 * from Model to View
	 * </p>
	 */
	void outgoingSetProgress(int current, int max);

	/**
	 * <p>
	 * from Model to View
	 * </p>
	 */
	void outgoingDone(boolean b);

	/**
	 * <p>
	 * from Model to View
	 * </p>
	 */
	void outgoingShutdown();

}
