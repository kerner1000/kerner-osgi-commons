package de.mpg.mpiz.koeln.kerner.sequencereader;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBean;

public class SequenceReaderActivator extends AbstractStep {

	@Override
	public boolean checkRequirements(DataBean data) {
		System.out.println("Wir checken!");
		return true;
	}

	@Override
	public void run(DataBean data) {
		System.out.println("Wir rennen!");
	}

	

}
