package de.fh.giessen.ringversuch.controller;

import de.fh.giessen.ringversuch.model.Model;
import de.fh.giessen.ringversuch.view.SwingView;
import de.fh.giessen.ringversuch.view.ViewIn;
import de.fh.giessen.ringversuch.view.ViewOut;

/**
 * <p>Application Controller.</p>
 * <p>Joins {@link ControllerIn} and {@link ControllerOut}</p>
 * @author Alexander Kerner
 * @lastVisit 2009-08-14
 *
 */
interface Controller extends ControllerIn, ControllerOut {

	// TODO: dont know why i cannot cast from SwingView to ViewIn
// 	void setViewIn(ViewIn view);
	void setViewIn(SwingView view);
 	
	void setModel(Model model);

}
