package de.fh.giessen.ringversuch.view.main;

public interface ViewMain {

	void printMessage(String message, boolean isError);

	void setOnline();

	void setReady();

	void setWorking();

	void setProgress(int current, int max);

}
