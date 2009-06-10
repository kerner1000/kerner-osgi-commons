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
import de.mpg.mpiz.koeln.kerner.dataproxy.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.dataproxy.DataProxy;

public class SequenceReaderActivator extends AbstractStep {
	
	private final File fasta = new File("/home/pcb/kerner/Dropbox/ref2.fasta");
	private final File gtf = new File("/home/pcb/kerner/Dropbox/ref2.gtf");
	
	@Override
	public boolean checkRequirements() {
		AbstractStep.LOGGER.debug(this, "no requirements needed");
		return true;
	}

	@Override
	public void run() throws Exception {
		doFasta(getDataProxy());
		doGtf(getDataProxy());
	}

	private void doGtf(DataProxy data) throws IOException, GTFFormatErrorException, DataBeanAccessException {
		try{
			AbstractStep.LOGGER.debug(this, "reading GTF file " + gtf);
			GTFFile gtfFile = new GTFFile(gtf);
			ArrayList<GTFElement> elements = gtfFile.getElements();
			AbstractStep.LOGGER.debug(this, "done reading gtf, updating data");
			data.setVerifiedGenesGtf(elements);
			AbstractStep.LOGGER.debug(this, "gtf data updated: " + data.getVerifiedGenesGtf());
			}catch(Throwable t){
				t.printStackTrace();
			}
		
	}

	private void doFasta(DataProxy data) throws IOException, DataBeanAccessException {
		try{
		AbstractStep.LOGGER.debug(this, "reading FASTA file " + fasta);
		FASTAFile fastaFile = new FASTAFile(fasta);
		ArrayList<FASTASequence> sequences = fastaFile.getSequences();
		AbstractStep.LOGGER.debug(this, "done reading fasta, updating data");
		data.setVerifiedGenesFasta(sequences);
		AbstractStep.LOGGER.debug(this, "fasta data updated: " + data.getVerifiedGenesFasta());
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

	

}
