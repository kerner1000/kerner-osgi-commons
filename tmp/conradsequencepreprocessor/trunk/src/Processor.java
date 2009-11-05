import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bioutils.AbstractSequence;
import de.bioutils.Range;
import de.bioutils.Utils;
import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.FASTAElementImpl;
import de.bioutils.fasta.FASTAFileBuilder;
import de.bioutils.fasta.FastaUtils;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.Type;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileBuilder;
import de.bioutils.gff3.file.GFF3FileImpl;

public class Processor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Processor.class);

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
			"/home/pcb/kerner/Desktop/test.fasta");
	private final static File f2 = new File(
			"/home/pcb/kerner/Desktop/new.fasta");

	// private final static File f3 = new
	// File("/home/proj/kerner/diplom/a.thaliana/TAIR9_GFF3_genes.gff");
	private final static File f3 = new File(
			"/home/pcb/kerner/Desktop/test2.gff");
	private final static File f4 = new File("/home/pcb/kerner/Desktop/new.gff");

	private final static int OFFSET = 4;
	private final static int LENGTH_THRESH = 100;

	public static void main(String[] args) {
		try {
			info("reading fasta file");
			final NewFASTAFile fastaFile = NewFASTAFileImpl.parse(f1);
			info("done reading fasta file");
			info("reading gff file");
			final GFF3File gff3File = GFF3Utils.convertFromGFFFile(f3, true);
			info("done reading gff file (elements:"
					+ gff3File.getElements().getSize() + ")");
			final FASTAFileBuilder fastaBuilder = new FASTAFileBuilder();
			fastaBuilder.setLineLength(10);
			final GFF3FileBuilder gffBuilder = new GFF3FileBuilder(gff3File
					.isSorted());

			info("get mapped genes");
			Map<GFF3Element, GFF3ElementGroup> map = gff3File.getElements()
					.getAllChildsForType(Type.mRNA);
			info("done get mapped genes");
			for (Entry<GFF3Element, GFF3ElementGroup> entry : map.entrySet()) {
				System.out.println("---------------------------");
				System.out.println(entry.getKey());
				for (GFF3Element e : entry.getValue()) {
					System.out.println("\t" + e);
				}
				info("range=" + entry.getKey().getRange());
				info("childs by index="
						+ GFF3Utils.elementsAreChildByIndex(entry.getKey(),
								entry.getValue()));
			}

			// gffBuilder.build().write(f4);
			// fastaBuilder.build().write(f2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
