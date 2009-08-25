package de.fh.giessen.ringversuch.view;

import java.awt.Component;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

public interface CentralViewService {

	void hideSettingsView();

	ViewTypeSettings getSettings();

	void showSettingsView();

	void setSettings(ViewTypeSettings settings);

	Component getMainComponent();

}
