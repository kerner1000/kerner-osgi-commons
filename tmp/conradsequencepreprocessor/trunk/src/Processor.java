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

	private final static File f1 = new File(
			"/home/pcb/kerner/Desktop/test.fasta");
	private final static File f2 = new File(
			"/home/pcb/kerner/Desktop/new.fasta");

	private final static File f3 = new File("/home/pcb/kerner/Desktop/test.gff");
	private final static File f4 = new File("/home/pcb/kerner/Desktop/new.gff");

	private final static int OFFSET = 4;
	private final static int LENGTH_THRESH = 100;

	public static void main(String[] args) {
		try {
			final NewFASTAFile fastaFile = NewFASTAFileImpl.parse(f1);
			final GFF3File gff3File = GFF3Utils.convertFromGFFFile(f3, true);
			final FASTAFileBuilder fastaBuilder = new FASTAFileBuilder();
			fastaBuilder.setLineLength(10);
			final GFF3FileBuilder gffBuilder = new GFF3FileBuilder(true);
			gffBuilder.setSorted(gff3File.isSorted());

			Map<? extends GFF3Element, Collection<? extends GFF3Element>> genes = GFF3Utils
					.getAllForType(gff3File, Type.gene);

			for (Entry<? extends GFF3Element, Collection<? extends GFF3Element>> e : genes
					.entrySet()) {
				System.out.println("key:" + "\t" + e.getKey() + Utils.NEW_LINE
						+ "values:");
				for (GFF3Element ee : e.getValue()) {
					System.out.println("\t" + ee);
				}
				System.out
						.println("parent-child structure is valid by ranges: "
								+ GFF3Utils.elementsAreChildByIndex(e.getKey(),
										e.getValue()));
				Range range = new Range(e.getKey().getStart(), e.getKey().getStop());
				System.out.println("overall range of this feature is "
						+ range + ",length=" + range.getLength());
				
//				if(range.getLength() > LENGTH_THRESH){
//					System.out.println("range for gene too large, must discard it");
//					continue;
//				}
				
				final FASTAElement fastaEl = fastaFile.getElementByHeader(e.getKey().getSeqID());
				System.out.println("corresponding fasta="+fastaEl.getHeader());
				
				if(fastaEl.getSequence().getLength() <= LENGTH_THRESH){
					// all good, no need to change nothing.
					if(!fastaBuilder.containsElement(fastaEl))
						fastaBuilder.addElement(fastaEl);
					
					gffBuilder.addElement(e.getKey());
					gffBuilder.addAllElements(e.getValue());
				} else {
					System.out.println("gg");
				}
			}
			
			System.out.println(fastaBuilder.build().getElements().iterator().next().getSequence().getLength());
			gffBuilder.build().write(f4);
			fastaBuilder.build().write(f2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
