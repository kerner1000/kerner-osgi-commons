package de.mpg.mpizkoeln.kerner.anna.sequencesreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

import de.kerner.commons.file.LazyStringReader;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBeanProxy;

public class SequencesReader extends AbstractStep {

	private final static String INPUT_FILE_FASTA = "input.file.fasta";
	private final static String INPUT_FILE_GTF = "input.file.gtf";

	@Override
	public boolean checkRequirements() {
		AbstractStep.LOGGER.debug(this, "Properties: " + getStepProperties());
		return new File(getStepProperties().getProperty(INPUT_FILE_FASTA))
				.canRead()
				&& new File(getStepProperties().getProperty(INPUT_FILE_GTF))
						.canRead();
	}

	@Override
	public void run() throws Exception {
		AbstractStep.LOGGER
				.debug(this, "We have been activated. Going to do our thing");
		readFASTA();
		readGTF();
	}

	private void readFASTA() throws Exception {
		try {
		File file = new File(getStepProperties().getProperty(INPUT_FILE_FASTA));
		AbstractStep.LOGGER.debug(this, "Reading FASTA-File from " + file);
		LazyStringReader reader = new LazyStringReader(file);
		AbstractStep.LOGGER.debug(this, "Done reading FASTA-File from " + file);
		FASTAFile fastaFile = new FASTAFile(reader.getString());
		final ArrayList<FASTASequence> sequences = fastaFile.getSequences();
		DataBeanProxy.setValidatedFASTASeqs(sequences);
		AbstractStep.LOGGER.debug(this, "Updated DataBean");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

	private void readGTF() throws IOException, GTFFormatErrorException {
		try {
			File file = new File(getStepProperties()
					.getProperty(INPUT_FILE_GTF));
			AbstractStep.LOGGER.debug(this, "Reading GTF-File from " + file);
			LazyStringReader reader = new LazyStringReader(file);
			AbstractStep.LOGGER.debug(this, "Done reading GTF-File from " + file);
			GTFFile gtfFile = new GTFFile(reader.getString());
			ArrayList<GTFElement> elements = gtfFile.getElements();
			DataBeanProxy.setValidatedGTFs(elements);
			AbstractStep.LOGGER.debug(this, "Updated DataBean");

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
