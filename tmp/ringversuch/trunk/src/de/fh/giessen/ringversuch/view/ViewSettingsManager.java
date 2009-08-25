package de.fh.giessen.ringversuch.view;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>
 * Implementation of {@code ViewSettingsManager} holds settings for {@code View}
 * </p>
 * @see ViewTypeSettings
 * @see View
 * @author Alexander Kerner
 * 
 */
public interface ViewSettingsManager {

	/**
	 * 
	 * @return settings for view.
	 */
	ViewTypeSettings get();

	/**
	 * 
	 * @param settings settings for view.
	 */
	void set(ViewTypeSettings settings);

}
