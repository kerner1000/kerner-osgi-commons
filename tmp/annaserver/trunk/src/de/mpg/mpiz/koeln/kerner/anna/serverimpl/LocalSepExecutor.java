package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBeanProvider;

class LocalSepExecutor extends AbstractStepExecutor {
	
	LocalSepExecutor(AbstractStep step, DataBeanProvider provider) {
		super(step, provider);
	}

	public Boolean call() throws Exception {
		waitForReq();
		run();
		System.out.println(this + ": step " + step + " done");
		synchronized (provider) {
			provider.notifyAll();
		}
		return true;
	}
	
	private void run() throws Exception {
			System.out.println(this + ": running step " + step);
			final DataBean data = step.run(super.provider.getDataProxy().getDataBean());
			System.out.println(this + ": step " + step + " finished, updateing data");
			super.provider.getDataProxy().updateDataBean(data);
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}
}
