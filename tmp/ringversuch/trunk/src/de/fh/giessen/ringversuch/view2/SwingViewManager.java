package de.fh.giessen.ringversuch.view2;

public interface SwingViewManager extends SwingView, ViewSettingsManager {
	
	void addView(ViewType type, SwingView view);
	
	void switchView(ViewState state);
}
