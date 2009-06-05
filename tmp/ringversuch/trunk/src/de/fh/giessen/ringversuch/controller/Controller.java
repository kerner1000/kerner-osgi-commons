package de.fh.giessen.ringversuch.controller;

import java.io.File;

public interface Controller {

	void printMessage(String message, boolean isError);
	void showError(String message);
	void setOutFile(File selectedFile);
	boolean settingsValid();


}
