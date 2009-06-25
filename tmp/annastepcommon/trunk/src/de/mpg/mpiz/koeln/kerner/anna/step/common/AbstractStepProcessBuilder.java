package de.mpg.mpiz.koeln.kerner.anna.step.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;

public abstract class AbstractStepProcessBuilder {
	
	protected final File executableDir, workingDir;
	private final LogDispatcher logger;
	
	protected AbstractStepProcessBuilder(File executableDir, File workingDir) {
		this.executableDir = executableDir;
		this.workingDir = workingDir;
		this.logger = null;
	}
	
	protected AbstractStepProcessBuilder(File stepWorkingDir, File executableDir, LogDispatcher logger) {
		this.executableDir = executableDir;
		this.workingDir = stepWorkingDir;
		this.logger = logger;
	}
	
	public boolean createAndStartProcess(final OutputStream out, final OutputStream err) {
		final List<String> processCommandList = getCommandList();
		final ProcessBuilder processBuilder = new ProcessBuilder(processCommandList);
		debug("creating process "
				+ processBuilder.command());
		processBuilder.directory(executableDir);
		debug("working dir of process: "
				+ processBuilder.directory());
		processBuilder.redirectErrorStream(true);
		try {
			Process p = processBuilder.start();
			debug("started process " + p);
			FileUtils.inputStreamToOutputStream(p.getInputStream(), out);
			FileUtils.inputStreamToOutputStream(p.getErrorStream(), err);
			final int exit = p.waitFor();
			debug("process " + p
					+ " exited with exit code " + exit);
			if (exit != 0)
				return false;
			return true;
		} catch (IOException e) {
			error(e.toString(), e);
			return false;
		} catch (InterruptedException e) {
			error(e.toString(), e);
			return false;
		}
	}
	
	public boolean createAndStartProcess() {
		return createAndStartProcess(System.out, System.err);
	}
	
	private void debug(String message){
		if(logger == null){
			System.out.println(this + ": " + message);
		} else {
			logger.debug(this, message);
		}
	}
	
	private void error(String message, Throwable t){
		if(logger == null){
			System.err.println(this + ": " + message);
		} else {
			logger.error(this, message, t);
		}
	}
	
	protected abstract List<String> getCommandList();

}
