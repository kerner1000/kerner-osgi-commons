package de.fh.giessen.ringversuch.model;

import de.fh.giessen.ringversuch.controller.Controller;

class WorkMonitor {
	
	private final Controller controller;
	
	WorkMonitor(Controller controller) {
		this.controller = controller;
	}
	
	void printMessage(String message){
		controller.printMessage(message, false);
	}
	
	void setProgress(int current, int max){
		controller.setProgress(current, max);
	}

}
