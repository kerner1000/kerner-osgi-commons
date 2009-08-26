package de.fh.giessen.ringversuch.model;

import de.fh.giessen.ringversuch.controller.ControllerOut;

class WorkMonitor {
	
	private final ControllerOut controller;
	
	WorkMonitor(ControllerOut controller) {
		this.controller = controller;
	}
	
	void printMessage(String message){
		controller.outgoingPrintMessage(message, false);
	}
	
	void setProgress(int current, int max){
		controller.outgoingSetProgress(current, max);
	}

}
