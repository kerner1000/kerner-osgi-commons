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
			"/home/proj/kerner/diplom/a.thaliana/TAIR9_chr1.fas");

	private final static File f2 = new File(
			"/home/proj/kerner/diplom/a.thaliana/TAIR9_chr1_new.fas");

	private final static File f3 = new File(
			"/home/proj/kerner/diplom/a.thaliana/TAIR9_GFF3_genes_chr1.gff");

	private final static File f4 = new File(
			"/home/proj/kerner/diplom/a.thaliana/TAIR9_GFF3_genes_chr1_new.gff");

	private final static int OFFSET = 100;
	private final static int LENGTH_THRESH = 100;

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
			final GFF3FASTAUnion unionTrimmed = union.trimmFastas(OFFSET);
			info("trimming fastas done");
			// debug("gff3 after trimming:" + Utils.NEW_LINE +
			// unionTrimmed.getGFF3ElementGroup());
			info("running union integrity test");
			unionTrimmed.checkIntegrity();
			info("running union integrity test successful");

			info("writing files \"" + f4.getName() + "\", \"" + f2.getName() + "\"");
			unionTrimmed.write(f4, f2);
			info("all done successfully!!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
