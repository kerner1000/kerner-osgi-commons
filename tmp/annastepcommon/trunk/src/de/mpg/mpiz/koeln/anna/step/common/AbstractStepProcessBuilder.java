package de.mpg.mpiz.koeln.anna.step.common;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;

// TODO make ThreadSave
public abstract class AbstractStepProcessBuilder {

	protected final File executableDir, workingDir;
	private final LogDispatcher logger;
	private final Map<File, Boolean> outFiles = new HashMap<File, Boolean>();

	protected AbstractStepProcessBuilder(File executableDir, File workingDir) {
		this.executableDir = executableDir;
		this.workingDir = workingDir;
		this.logger = new ConsoleLogger();
	}

	protected AbstractStepProcessBuilder(File executableDir, File workingDir,
			LogDispatcher logger) {
		this.executableDir = executableDir;
		this.workingDir = workingDir;
		this.logger = logger;
	}

	public void addResultFile(boolean takeShortCutIfAlreadyThere,
			String fileName) {
		addResultFile(takeShortCutIfAlreadyThere, new File(workingDir, fileName));
	}
	
//	public void addAllResultFile(Map<String, Boolean> m) {
//		if(m.isEmpty())
//			return;
//		for(Entry<String, Boolean> e : m.entrySet()){
//			addResultFile(e.getValue(), new File(workingDir, e.getKey()));
//		}
//	}
	
	public void addAllResultFiles(Map<File, Boolean> m) {
		if(m.isEmpty())
			return;
		outFiles.putAll(m);
	}
	
	public void addResultFile(boolean takeShortCutIfAlreadyThere, File file) {
		if (file == null)
			throw new NullPointerException(
					"file must not be null");
		outFiles
				.put(file, takeShortCutIfAlreadyThere);
	}

	private boolean takeShortCut() {
		logger.debug(this, "checking for shortcut available");
		if(outFiles.isEmpty()){
			logger.debug(this, "no outfiles defined");
			return false;
		}
		for (Entry<File, Boolean> e : outFiles.entrySet()) {
			final boolean fileCheck = FileUtils.fileCheck(e.getKey(), false);
			final boolean skipIt = e.getValue();
			logger.debug(this, "file " + e.getKey().getAbsolutePath() + " there="+fileCheck);
			logger.debug(this, "file " + e.getKey() + " skip="+skipIt);
			if(!(fileCheck && skipIt)){
				logger.debug(this, "cannot skip");
				return false;
			}
		}
		logger.debug(this, "skip available");
		return true;
	}

	public boolean createAndStartProcess(final OutputStream out,
			final OutputStream err) {
		if (takeShortCut()){
			logger.info(this, "file(s) there, taking shortcut");
			return true;
		}
		logger.debug(this, "file(s) not there, cannot take shortcut");
		final List<String> processCommandList = getCommandList();
		final ProcessBuilder processBuilder = new ProcessBuilder(
				processCommandList);
		logger.debug(this, "creating process " + processBuilder.command());
		processBuilder.directory(executableDir);
		logger.debug(this, "executable dir of process: " + processBuilder.directory());
		processBuilder.redirectErrorStream(true);
		try {
			Process p = processBuilder.start();
			logger.debug(this, "started process " + p);
			FileUtils.inputStreamToOutputStream(p.getInputStream(), out);
			FileUtils.inputStreamToOutputStream(p.getErrorStream(), err);
			final int exit = p.waitFor();
			logger.debug(this, "process " + p + " exited with exit code " + exit);
			if (exit != 0)
				return false;
			return true;
		} catch (Exception e){
			e.printStackTrace();
			logger.error(this, e.getLocalizedMessage(), e);
			return false;
		}
	}

	public boolean createAndStartProcess() {
		return createAndStartProcess(System.out, System.err);
	}
	
	protected abstract List<String> getCommandList();

	

}
