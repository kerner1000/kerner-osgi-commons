package de.fh.giessen.ringversuch.view;

/**
 * <p>
 * Implementation of {@code SwingViewManager} is the central contact point to
 * comunicate to any Implementation of{@code ViewIn} or {@code ViewOut}.
 * </p>
 * <p>
 * {@code SwingViewManager} also brings functionality to manage different types
 * of view.
 * </p>
 * 
 * @see ViewType
 * @see ViewOut
 * @see ViewIn
 * @see ViewState
 * @see SwingView
 * @author Alexander Kerner
 * @lastVisit 2009-08-26
 * 
 */
public interface SwingViewManager extends SwingView, ViewSettingsManager {

	void addView(ViewType type, SwingView view);

	void switchView(ViewState state);
}
