package de.mpg.mpizkoeln.kerner.anna.sequencesreader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

import de.kerner.commons.file.LazyStringReader;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBean;

public class SequencesReader extends AbstractStep {

	private final static String INPUT_FILE_FASTA = "input.file.fasta";
	private final static String INPUT_FILE_GTF = "input.file.gtf";

	@Override
	public boolean checkRequirements(DataBean data) {
		AbstractStep.LOGGER.debug("Properties: " + getStepProperties());
		return new File(getStepProperties().getProperty(INPUT_FILE_FASTA))
				.canRead()
				&& new File(getStepProperties().getProperty(INPUT_FILE_GTF))
						.canRead();
	}

	@Override
	public DataBean run(DataBean data) throws Exception {
		AbstractStep.LOGGER
				.debug("We have been activated. Going to do our thing");
		readFASTA(data);
		readGTF(data);
		return data;
	}

	private void readGTF(DataBean data) throws IOException {
		
		File file = new File(getStepProperties().getProperty(INPUT_FILE_FASTA));
		AbstractStep.LOGGER.debug("Reading FASTA-File from " + file);
		
		LazyStringReader reader = new LazyStringReader(file);
		
		AbstractStep.LOGGER.debug("Done reading FASTA-File from " + file);
		FASTAFile fastaFile = new FASTAFile(reader.getString());
		Collection<FASTASequence> sequences = fastaFile.getSequences();
		data.setValidatedFASTASeqs(sequences);
		AbstractStep.LOGGER.debug("Updated DataBean");
		

	}

	private void readFASTA(DataBean data) throws IOException,
			GTFFormatErrorException {
		try{
		File file = new File(getStepProperties().getProperty(INPUT_FILE_GTF));
		AbstractStep.LOGGER.debug("Reading GTF-File from " + file);
		LazyStringReader reader = new LazyStringReader(file);
		AbstractStep.LOGGER.debug("Done reading GTF-File from " + file);
		GTFFile gtfFile = new GTFFile(reader.getString());
		Collection<GTFElement> elements = gtfFile.getElements();
		data.setValidatedGTFs(elements);
		AbstractStep.LOGGER.debug("Updated DataBean");
		
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
}
