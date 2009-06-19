package de.mpg.mpiz.koeln.kerner.anna.step.conrad.common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.kerner.commons.file.FileUtils;

public abstract class AbstractRunState {
	
	protected final File conradWorkingDir, stepWorkingDir;
	
	protected AbstractRunState(File stepWorkingDir, File conradWorkingDir) {
		this.conradWorkingDir = conradWorkingDir;
		this.stepWorkingDir = stepWorkingDir;
	}
	
	protected boolean createAndStartProcess() {
		final List<String> conradCmdList = getCommandList();
		final ProcessBuilder processBuilder = new ProcessBuilder(conradCmdList);
		System.out.println(this + " creating process "
				+ processBuilder.command());
		processBuilder.directory(conradWorkingDir);
		System.out.println(this + " working dir of process: "
				+ processBuilder.directory());
		processBuilder.redirectErrorStream(true);
		try {
			Process p = processBuilder.start();
			System.out.println(this + " started process " + p);
			FileUtils.inputStreamToOutputStream(p.getInputStream(), System.out);
			FileUtils.inputStreamToOutputStream(p.getErrorStream(), System.err);
			final int exit = p.waitFor();
			System.out.println(this + " process " + p
					+ " exited with exit code " + exit);
			if (exit != 0)
				return false;
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	protected abstract List<String> getCommandList();

}
