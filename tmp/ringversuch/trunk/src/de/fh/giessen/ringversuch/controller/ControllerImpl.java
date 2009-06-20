package de.fh.giessen.ringversuch.controller;

import java.io.File;

import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.model.ModelImpl;
import de.fh.giessen.ringversuch.view.View;
import de.fh.giessen.ringversuch.view.ViewImpl;

public class ControllerImpl implements Controller {

	private static String LOG_PROPERTIES = "C:\\Users\\juli\\log.properties";
	
	@Override
	public void printMessage(String message, boolean isError) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutFile(File selectedFile) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean settingsValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showError(String message) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args){
		View view = new ViewImpl(new ControllerImpl());
		view.setOnline();
		
		
		
	}

}
