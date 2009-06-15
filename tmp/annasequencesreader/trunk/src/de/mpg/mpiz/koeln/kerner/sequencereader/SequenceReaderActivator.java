package de.mpg.mpiz.koeln.kerner.sequencereader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBean;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBeanAccessException;

public class SequenceReaderActivator extends AbstractStep {
	
	private final File fasta = new File("/home/pcb/kerner/Dropbox/ref2.fasta");
	private final File gtf = new File("/home/pcb/kerner/Dropbox/ref2.gtf");
	
	@Override
	public boolean checkRequirements(DataBean dataBean) {
		System.out.println("no requirements needed");
		return true;
	}

	@Override
	public void run(DataBean dataBean) throws Exception {
		doFasta(dataBean);
		doGtf(dataBean);
	}

	private void doGtf(DataBean data) throws IOException, GTFFormatErrorException, DataBeanAccessException {
		try{
			System.out.println("reading GTF file " + gtf);
			GTFFile gtfFile = new GTFFile(gtf);
			ArrayList<GTFElement> elements = gtfFile.getElements();
			System.out.println("done reading gtf, updating data");
			data.setVerifiedGenesGtf(elements);
			System.out.println("gtf data updated: " + data.getVerifiedGenesGtf());
			}catch(Throwable t){
				t.printStackTrace();
			}
		
	}

	private void doFasta(DataBean data) throws IOException, DataBeanAccessException {
		try{
		System.out.println("reading FASTA file " + fasta);
		FASTAFile fastaFile = new FASTAFile(fasta);
		ArrayList<FASTASequence> sequences = fastaFile.getSequences();
		System.out.println("done reading fasta, updating data");
		data.setVerifiedGenesFasta(sequences);
		System.out.println("fasta data updated: " + data.getVerifiedGenesFasta());
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

	

}
