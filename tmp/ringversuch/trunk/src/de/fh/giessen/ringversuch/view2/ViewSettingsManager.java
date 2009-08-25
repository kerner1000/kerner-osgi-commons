package de.fh.giessen.ringversuch.view2;

import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

public interface ViewSettingsManager {
	
	ViewTypeSettings get();
	
	void set(ViewTypeSettings settings);

}
