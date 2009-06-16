package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBeanProvider;

class LocalSepExecutor extends AbstractStepExecutor {
	
	LocalSepExecutor(AbstractStep step, DataBeanProvider provider) {
		super(step, provider);
	}

	public Boolean call() throws Exception {
		waitForReq();
		run();
		System.out.println(this + ": step " + step + " done");
		notifyAll();
		return true;
	}
	
	private void run() throws Exception {
		synchronized (step) {
			System.out.println(this + ": running step " + step);
			step.run(super.provider.getDataProxy().getDataBean());
		}
	}
}
