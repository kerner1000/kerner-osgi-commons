package de.mpg.mpiz.koeln.kerner.anna.serverimpl;

import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.Server;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;

class LocalSepExecutor extends AbstractStepExecutor {

	LocalSepExecutor(AbstractStep step, Server server) {
		super(step, server);
	}

	public Boolean call() throws Exception {
		waitForReq();
		final boolean b = checkNeedToRun();
		if (b) {
			System.out.println(this + ": step " + step
					+ " needs to run, executing");
			run();
		} else {
			System.out.println(this + ": step " + step
					+ " does not need to run");
		}
		System.out.println(this + ": step " + step + " done");
		if (b) {
			synchronized (server) {
				server.notifyAll();
			}
		}
		return true;
	}

	private void run() throws Exception {
		System.out.println(this + ": running step " + step);
		final DataBean data = step.run(super.server.getDataProxyProvider()
				.getDataProxy().getDataBean());
		System.out.println(this + ": step " + step
				+ " finished, updateing data");
		super.server.getDataProxyProvider().getDataProxy().updateDataBean(data);
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
