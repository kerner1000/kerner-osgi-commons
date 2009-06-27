package de.fh.giessen.ringversuch.view;


public interface View {
	
	void printMessage(String message, boolean isError);

	void setOnline();

	void setWorking();
	
}
