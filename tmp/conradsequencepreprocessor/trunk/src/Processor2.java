import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bioutils.Utils;
import de.bioutils.fasta.FastaUtils;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff3.GFF3FASTAUnion;
import de.bioutils.gff3.GFF3FASTAUnionImpl;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.Type;
import de.bioutils.gff3.file.GFF3File;

public class Processor2 {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Processor2.class);

	private static void warn(Object msg) {
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(msg.toString());
		}
	}

	private static void debug(Object msg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(msg.toString());
		}
	}

	private static void info(Object msg) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(msg.toString());
		}
	}

	private final static File f1 = new File(
			"/home/proj/kerner/diplom/f.graminearum/fusarium_graminearum_3_supercontigs.fasta");

	private final static File f2 = new File(
			"/home/proj/kerner/diplom/f.graminearum/fusarium_graminearum_3_supercontigs_new.fasta");

	private final static File f3 = new File(
			"/home/proj/kerner/diplom/f.graminearum/fusarium_graminearum_3_transcripts.gtf");

	private final static File f4 = new File(
			"/home/proj/kerner/diplom/f.graminearum/fusarium_graminearum_3_transcripts_new.gtf");

	private final static int OFFSET = 200;
	private final static int NUM_TRESH = 5000;
	private final static int LENGTH_TRESH = 5000;

	public static void main(String[] args) {
		try {
			info("reading fasta file \"" + f1.getName() + "\"");
			NewFASTAFile fastaFile = NewFASTAFileImpl.parse(f1);
			final String tmpHeader = fastaFile.getElements().iterator().next().getHeader();
			info("done reading fasta file");
			info("trimming fasta headers");
			fastaFile = FastaUtils.trimHeader(fastaFile);
			info("done trimming fasta headers");
			debug("old header: \"" + tmpHeader + "\", new header: \"" + fastaFile.getElements().iterator().next().getHeader() + "\"");
			info("reading gff file \"" + f3.getName() + "\"");
			final GFF3File gff3File = GFF3Utils.convertFromGFFFile(f3, true);
			info("done reading gff file (elements:"
					+ gff3File.getElements().getSize() + ")");

			info("creating Union");
			final GFF3FASTAUnion union = new GFF3FASTAUnionImpl(gff3File,
					fastaFile);
			info("running union integrity test");
			union.checkIntegrity();
			info("running union integrity test successful");
			info("trimming fasta element length (Offset:" + OFFSET + ")");
			GFF3FASTAUnion unionTrimmed = null;
			if(union.containsElementOfType(Type.gene)){
				debug("trimming by type \"" + Type.gene + "\"");
				unionTrimmed = union.trimmFastasByType(OFFSET, Type.gene);
			} else {
				debug("trimming by attribute");
				unionTrimmed = union.trimmFastasByAttribute(OFFSET);
			}
			
			info("trimming fastas done");
			// debug("gff3 after trimming:" + Utils.NEW_LINE +
			// unionTrimmed.getGFF3ElementGroup());
			info("running union integrity test");
			unionTrimmed.checkIntegrity();
			info("running union integrity test successful");
			
			info("discarding all elements with range >  " + LENGTH_TRESH);
			GFF3FASTAUnion unionReducedLength = unionTrimmed.trimmMaxElementLength(LENGTH_TRESH);
			unionReducedLength.checkIntegrity();
			info("discarded " + (unionTrimmed.getSize() - unionReducedLength.getSize()) + " elements");
			
			info("reducing size to " + NUM_TRESH);
			GFF3FASTAUnion unionReducedSize = unionReducedLength.reduceSize(NUM_TRESH);
			unionReducedSize.checkIntegrity();
			info("discarded " + (unionReducedLength.getSize() - unionReducedSize.getSize()) + " elements");
			
			info("length of longest FASTA: " + unionReducedSize.getFASTAElements().getLargestElement().getSequence().getLength());
			info("length of longest GFF3: " + unionReducedSize.getGFF3ElementGroup().getLargestElement().getRange());
			
			info("writing files \"" + f4.getName() + "\", \"" + f2.getName() + "\"");
			unionReducedSize.write(f4, f2);
			info("all done successfully!!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
