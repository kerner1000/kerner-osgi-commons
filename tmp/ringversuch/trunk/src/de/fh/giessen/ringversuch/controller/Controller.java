package de.fh.giessen.ringversuch.controller;

import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.view.View;

interface Controller extends ControllerIn, ControllerOut {

 	void setView(View view);
 	
	void setModel(Model model);

}
