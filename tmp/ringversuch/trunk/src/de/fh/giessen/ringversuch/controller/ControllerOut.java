package de.fh.giessen.ringversuch.controller;

/**
 * 
 * Handle Events from Model to View.
 * 
 * @author Alexander Kerner
 *  
 */
public interface ControllerOut {

	/**
	 * Out
	 * 
	 * @param message
	 * @param isError
	 */
	void printMessage(String message, boolean isError);

	/**
	 * Out
	 * 
	 * @param message
	 */
	void showError(String message);

	/**
	 * Out
	 * 
	 * @param current
	 * @param max
	 */
	void setProgress(int current, int max);

	/**
	 * Out
	 * 
	 * @param b
	 */
	void done(boolean b);

}
