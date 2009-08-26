package de.fh.giessen.ringversuch.view;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>Handle outgoing events (from Model bzw. Controller to View).</p>
 * @author Alexander Kerner
 *
 */
public interface ViewOut {

	/**
	 * from Model to View
	 */
	void outgoingPrintMessage(String message, boolean isError);

	/**
	 * from Model to View
	 */
	void outgoingSetOnline();
	
	/**
	 * from Model to View
	 */
	void outgoingShutdown();

	/**
	 * from Model to View
	 */
	void outgoingSetWorking();
	
	/**
	 * from Model to View
	 */
	void outgoingSetReady();

	/**
	 * from Model to View
	 */
	void outgoingShowError(String message);

	/**
	 * from Model to View
	 */
	ViewTypeSettings outgoingSetSettings();
	
	/**
	 * from Model to View
	 */
	void outgoingSetSettings(ViewTypeSettings settings);

	/**
	 * from Model to View
	 */
	void outgoingSetProgress(int current, int max);
	
}
