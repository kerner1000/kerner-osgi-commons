package de.fh.giessen.ringversuch.view2;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

public interface SettingsContent extends Content {

	ViewTypeSettings getSettings();

	void setSettings(ViewTypeSettings settings);

	

}
