package de.mpg.mpiz.koeln.kerner.anna.server;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;

public interface Server {

	public void registerStep(AbstractStep step);
	public void unregisterStep(AbstractStep step);
	
}
