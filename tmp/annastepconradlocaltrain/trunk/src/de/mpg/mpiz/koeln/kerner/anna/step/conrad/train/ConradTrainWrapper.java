package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.commons.file.FileUtils;
import de.kerner.commons.file.LazyStringWriter;

class ConradTrainWrapper {

	private final static File CONRAD_DIR = new File(
			"/opt/share/local/users/kerner/conrad/conradV1");
	private final static File WORKING_DIR = new File(
			"/home/pcb/kerner/Desktop/contradTmpDir/");
	private final static String TRAINING_FILE_NAME = "trainingFile.bin";
	private final static String FASTA_FILE_NAME = "ref.fasta";
	private final static String GTF_FILE_NAME = "ref.gtf";
	private final static String CONRAD_EXE = "bin/conrad.sh";
	private final ArrayList<? extends FASTASequence> fastas;
	private final ArrayList<? extends GTFElement> elements;
	private File result = null;

	ConradTrainWrapper(ArrayList<? extends FASTASequence> fastas,
			ArrayList<? extends GTFElement> elements) {
		this.fastas = fastas;
		this.elements = elements;
	}
	
	File getResult(){
		return result;
	}

	boolean run() throws Exception {
		if (!WORKING_DIR.exists()) {
			System.out.println(this + ": " + WORKING_DIR
					+ " does not exist, creating");
			final boolean b = WORKING_DIR.mkdirs();
			if (b == false) {
				System.err.println("!!! Hier abbrechen.... !!!");
			}
		}
		writeDataToDir();
		final List<String> conradCmdList = getConradCommandList();
		final ProcessBuilder processBuilder = new ProcessBuilder(conradCmdList);
		System.out.println(this + " creating process " + processBuilder.command());
		processBuilder.directory(CONRAD_DIR);
		System.out.println(this + " working dir of process: " + processBuilder.directory());
		processBuilder.redirectErrorStream(true);
		final Process p = processBuilder.start();
		System.out.println(this + " started process " + p);
		FileUtils.inputStreamToOutputStream(p.getInputStream(), System.out);
		FileUtils.inputStreamToOutputStream(p.getErrorStream(), System.err);
		final int exit = p.waitFor();
		System.out.println(this + " process " + p + " exited with exit code "
				+ exit);
		if(exit != 0)
			return false;
		final File result = new File(CONRAD_DIR, TRAINING_FILE_NAME);
		this.result = result;
		return true;
	}

	private void writeDataToDir() throws IOException {
		final FASTAFile fastaFile = new FASTAFile(fastas);
		fastaFile.setLineLength(60);
		final File file = new File(WORKING_DIR, FASTA_FILE_NAME);
		System.out.println(this + " writing fastas to " + file);
		fastaFile.writeToFile(file);

		final GTFFile gtfFile = new GTFFile(elements);
		final File file2 = new File(WORKING_DIR, GTF_FILE_NAME);
		System.out.println(this + " writing gtf to " + file2);
		gtfFile.writeToFile(file2);
	}

	private List<String> getConradCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(new File(CONRAD_DIR,CONRAD_EXE).getAbsolutePath());
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(WORKING_DIR.getAbsolutePath());
		final File trainingFile = new File(WORKING_DIR, TRAINING_FILE_NAME);
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		return builder.getCommandList();
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
