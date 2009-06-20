package de.fh.giessen.ringversuch.view;

public interface View {
	
	void appendLog(String message, boolean isError);

	void setOnline();
	
}
