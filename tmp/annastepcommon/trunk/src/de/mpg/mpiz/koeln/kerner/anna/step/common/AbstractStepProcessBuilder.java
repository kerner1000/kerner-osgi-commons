package de.mpg.mpiz.koeln.kerner.anna.step.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;

public abstract class AbstractStepProcessBuilder {

	protected final File executableDir, workingDir;
	private final LogDispatcher logger;
	private final List<File> inFiles = new ArrayList<File>();
	private final Map<File, Boolean> outFiles = new HashMap<File, Boolean>();

	protected AbstractStepProcessBuilder(File executableDir, File workingDir) {
		this.executableDir = executableDir;
		this.workingDir = workingDir;
		this.logger = null;
	}

	protected AbstractStepProcessBuilder(File stepWorkingDir,
			File executableDir, LogDispatcher logger) {
		this.executableDir = executableDir;
		this.workingDir = stepWorkingDir;
		this.logger = logger;
	}

	public void addTempInFile(File file) throws FileNotFoundException {
		if (!FileUtils.fileCheck(file, false))
			throw new FileNotFoundException("file " + file + " does not exist");
		file.deleteOnExit();
		inFiles.add(file);
	}

	public void addTempInFile(String fileName) throws FileNotFoundException {
		addTempInFile(new File(workingDir, fileName));
	}

	public void addResultFile(boolean takeShortCutIfAlreadyThere,
			String fileName) {
		addResultFile(takeShortCutIfAlreadyThere, new File(workingDir, fileName));
	}
	
	public void addResultFile(boolean takeShortCutIfAlreadyThere, File file) {
		if (file == null)
			throw new NullPointerException(
					"file must not be null");
		outFiles
				.put(file, takeShortCutIfAlreadyThere);
	}

	private boolean takeShortCut() {
		if(outFiles.isEmpty())
			return false;
		for (Entry<File, Boolean> e : outFiles.entrySet()) {
			if(!(FileUtils.fileCheck(e.getKey(), false) && e.getValue()))
				return false;
		}
		return true;
	}

	public boolean createAndStartProcess(final OutputStream out,
			final OutputStream err) {
		if (takeShortCut())
			return true;
		final List<String> processCommandList = getCommandList();
		final ProcessBuilder processBuilder = new ProcessBuilder(
				processCommandList);
		debug("creating process " + processBuilder.command());
		processBuilder.directory(executableDir);
		debug("working dir of process: " + processBuilder.directory());
		processBuilder.redirectErrorStream(true);
		try {
			Process p = processBuilder.start();
			debug("started process " + p);
			FileUtils.inputStreamToOutputStream(p.getInputStream(), out);
			FileUtils.inputStreamToOutputStream(p.getErrorStream(), err);
			final int exit = p.waitFor();
			debug("process " + p + " exited with exit code " + exit);
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

	private void debug(String message) {
		if (logger == null) {
			System.out.println(this + ": " + message);
		} else {
			logger.debug(this, message);
		}
	}

	private void error(String message, Throwable t) {
		if (logger == null) {
			System.err.println(this + ": " + message);
		} else {
			logger.error(this, message, t);
		}
	}

	protected abstract List<String> getCommandList();

	

}
