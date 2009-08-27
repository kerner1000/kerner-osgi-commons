package de.fh.giessen.ringversuch.view;

import java.io.File;

/**
 * <p>
 * {@code MainContent} provides all functionality, that is typical for
 * {@link MainView}.
 * </p>
 * 
 * @see MainView
 * @author Alexander Kerner
 * 
 */
public interface MainContent extends Content {

	void printMessage(String message, boolean isError);

	void setOnline();

	void setReady();

	void setWorking();

	void setProgress(int current, int max);

	void showError(String message);

	void outgoingSetSelectedFiles(File[] files);

}
