package de.fh.giessen.ringversuch.view;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>
 * {@code SettingsContent} provides all functionality, that is typical for
 * {@link SettingsView}.
 * </p>
 * 
 * @see SettingsView
 * @author Alexander Kerner
 * 
 */
public interface SettingsContent extends Content {

	ViewTypeSettings getSettings();

	void setSettings(ViewTypeSettings settings);

	

}
