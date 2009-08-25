package de.fh.giessen.ringversuch.view2;

public interface MainContent extends Content {

	void printMessage(String message, boolean isError);

	void setOnline();

	void setReady();

	void setWorking();

	void setProgress(int current, int max);

	void showError(String message);

}
