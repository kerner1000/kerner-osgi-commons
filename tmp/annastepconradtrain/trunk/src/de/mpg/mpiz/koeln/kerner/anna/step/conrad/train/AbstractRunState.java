package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;

import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;

abstract class AbstractRunState {
	
	protected final static String FASTA_FILE_NAME = "ref.fasta";
	protected final static String GTF_FILE_NAME = "ref.gtf";
	protected final static String CONRAD_EXE = "bin/conrad.sh";
	private File result = null;
	protected final File workingDir, conradWorkingDir;
	protected final String trainingFileName;
	
	AbstractRunState(File workingDir, File conradWorkingDir, String trainingFileName){
		this.conradWorkingDir = conradWorkingDir;
		this.workingDir = workingDir;
		this.trainingFileName = trainingFileName;
	}

	boolean run(ArrayList<? extends FASTASequence> fastas,
			ArrayList<? extends GTFElement> elements) throws StepExecutionException {
		if (!writeDataToDir(fastas, elements)) {
			return false;
		}
		return createAndStartProcess();
	}

	private boolean createAndStartProcess() {
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
			final File result = new File(workingDir, trainingFileName);
			this.result = result;
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

	abstract List<String> getCommandList();

	private boolean writeDataToDir(ArrayList<? extends FASTASequence> fastas,
			ArrayList<? extends GTFElement> elements) {
		final FASTAFile fastaFile = new FASTAFile(fastas);
		fastaFile.setLineLength(60);
		final File file = new File(workingDir, FASTA_FILE_NAME);
		final GTFFile gtfFile = new GTFFile(elements);
		final File file2 = new File(workingDir, GTF_FILE_NAME);
		try {
			System.out.println(this + " writing fastas to " + file);
			fastaFile.writeToFile(file);
			System.out.println(this + " writing gtf to " + file2);
			gtfFile.writeToFile(file2);
			return true;
		} catch (IOException e) {
			System.out.println(this + ": error while creating files: " + file
					+ ", " + file2);
			return false;
		}
	}

	File getResult() {
		return result;
	}

}
