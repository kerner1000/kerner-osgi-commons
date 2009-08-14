package de.fh.giessen.ringversuch.controller;

import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.view.View;

/**
 * <p>Application Controller.</p>
 * <p>Joins {@link ControllerIn} and {@link ControllerOut}</p>
 * @author Alexander Kerner
 * @lastVisit 2009-08-14
 *
 */
interface Controller extends ControllerIn, ControllerOut {

 	void setView(View view);
 	
	void setModel(Model model);

}
