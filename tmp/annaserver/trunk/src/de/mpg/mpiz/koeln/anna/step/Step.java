package de.mpg.mpiz.koeln.anna.step;

import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;

public interface Step {

	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @return
	 * @throws StepExecutionException
	 */
	boolean requirementsSatisfied(DataProxy data) throws StepExecutionException;

	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @return
	 * @throws StepExecutionException
	 */
	boolean canBeSkipped(DataProxy data) throws StepExecutionException;

	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @return
	 * @throws StepExecutionException
	 */
	boolean run(DataProxy data) throws StepExecutionException;

	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @param listener
	 * @return
	 * @throws StepExecutionException
	 */
	boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException;

}
