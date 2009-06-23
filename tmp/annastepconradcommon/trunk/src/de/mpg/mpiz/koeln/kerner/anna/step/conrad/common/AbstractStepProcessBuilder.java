package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;

public abstract class AbstractStepProcessBuilder {
	
	protected final File executableDir, stepWorkingDir;
	protected final LogDispatcher logger;
	
	protected AbstractStepProcessBuilder(File stepWorkingDir, File executableWorkingDir) {
		this.executableDir = executableWorkingDir;
		this.stepWorkingDir = stepWorkingDir;
		this.logger = null;
	}
	
	protected AbstractStepProcessBuilder(File stepWorkingDir, File executableDir, LogDispatcher logger) {
		this.executableDir = executableDir;
		this.stepWorkingDir = stepWorkingDir;
		this.logger = logger;
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
	
	protected boolean createAndStartProcess() {
		final List<String> conradCmdList = getCommandList();
		final ProcessBuilder processBuilder = new ProcessBuilder(conradCmdList);
		debug("creating process "
				+ processBuilder.command());
		processBuilder.directory(executableDir);
		debug("working dir of process: "
				+ processBuilder.directory());
		processBuilder.redirectErrorStream(true);
		try {
			Process p = processBuilder.start();
			debug("started process " + p);
			FileUtils.inputStreamToOutputStream(p.getInputStream(), System.out);
			FileUtils.inputStreamToOutputStream(p.getErrorStream(), System.err);
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
	
	protected abstract List<String> getCommandList();

}
