package de.fh.giessen.ringversuch.view2;

import de.fh.giessen.ringversuch.controller.ControllerIn;

/**
 * <p>Handle outgoing events (from View bzw. User to Model bzw. Controller).</p>
 * <p>Executing thread is always atw event thread.<p>
 * @author Alexander Kerner
 *
 */
public interface ViewOut extends ControllerIn {
	
	// extending ControllerIn is only because im lazy. This is NOT how you should do this...

}
