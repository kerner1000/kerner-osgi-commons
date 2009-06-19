package de.mpg.mpiz.koeln.kerner.anna.step.sequencereader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

import de.mpg.mpiz.koeln.kerner.anna.core.StepExecutionException;
import de.mpg.mpiz.koeln.kerner.anna.other.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.other.StepProcessListener;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

public class StepSequenceReader extends AbstractStep {

	private final static String FASTA_KEY = "fasta";
	private final static String GTF_KEY = "gtf";
	private final static String DEFAULT_FASTA_PATH = "/home/pcb/kerner/Dropbox/ref2.fasta";
	private final static String DEFAULT_GTF_PATH = "/home/pcb/kerner/Dropbox/ref2.gtf";
	// TODO to external properties
	private final File fasta;
	private final File gtf;

	public StepSequenceReader() {
		final String fastaPath = super.getStepProperties().getProperty(
				FASTA_KEY, DEFAULT_FASTA_PATH);
		System.out.println(this + ": got path for FASTA: " + fastaPath);
		final String gtfPath = super.getStepProperties().getProperty(GTF_KEY,
				DEFAULT_GTF_PATH);
		System.out.println(this + ": got path for GTF: " + gtfPath);
		fasta = new File(fastaPath);
		gtf = new File(gtfPath);
	}

	@Override
	public boolean checkRequirements(DataBean dataBean) {
		System.out.println(this + ": no requirements needed");
		return true;
	}

	@Override
	public DataBean run(DataBean dataBean, StepProcessListener listener) throws StepExecutionException {
		try{
		dataBean = doFasta(dataBean);
		dataBean = doGtf(dataBean);
		setSuccess(true);
		}catch(Throwable t){
			t.printStackTrace();
		}
		return dataBean;
	}

	private DataBean doGtf(DataBean data) throws IOException,
			GTFFormatErrorException, DataBeanAccessException {
		// TODO remove try catch
		try {
			System.out.println(this + ": reading GTF file " + gtf);
			final GTFFile gtfFile = new GTFFile(gtf);
			final ArrayList<? extends GTFElement> elements = gtfFile.getElements();
			System.out.println(this + ": done reading gtf");
			data.setVerifiedGenesGtf(elements);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return data;
	}

	private DataBean doFasta(DataBean data) throws IOException,
			DataBeanAccessException {
		// TODO remove try catch
		try {
			System.out.println(this + ": reading FASTA file " + fasta);
			final FASTAFile fastaFile = new FASTAFile(fasta);
			final ArrayList<? extends FASTASequence> sequences = fastaFile.getSequences();
			System.out.println(this + ": done reading fasta");
			data.setVerifiedGenesFasta(sequences);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return data;
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean needToRun(DataBean data) throws StepExecutionException {
		try {
			return (data.getVerifiedGenesFasta() != null && data.getVerifiedGenesGtf() != null);
		} catch (DataBeanAccessException e) {
			throw new StepExecutionException(e);
		}
	}
}
