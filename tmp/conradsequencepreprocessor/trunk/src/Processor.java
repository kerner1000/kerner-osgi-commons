import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bioutils.AbstractSequence;
import de.bioutils.Utils;
import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.FASTAElementImpl;
import de.bioutils.fasta.FASTAFileBuilder;
import de.bioutils.fasta.FastaUtils;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff.element.GFFElementBuilder;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.Type;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.GFF3ElementBuilder;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.element.filter.GFF3ElementTypeFilter;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileBuilder;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.bioutils.range.ExpandedRange;
import de.bioutils.range.Range;
import de.bioutils.range.ShiftedRange;
import de.kerner.commons.collection.ArrayMap;

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
			"/home/alex/Dropbox/mpiz/thal/TAIR9_chr1.fas");
	private final static File f2 = new File(
			"/home/alex/Dropbox/mpiz/thal/new.fasta");

	private final static File f3 = new File(
			"/home/alex/Dropbox/mpiz/thal/test2.gff");
	private final static File f4 = new File(
			"/home/alex/Dropbox/mpiz/thal/new.gff");

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
			
			
			
			info("get genes");
			final List<GFF3ElementGroup> genes = gff3File.getElements()
					.getAllChildsForTypeList(Type.gene);
			info("get genes done");
			final FASTAFileBuilder fastaBuilder = new FASTAFileBuilder();
			// fastaBuilder.setLineLength(10);
			final GFF3FileBuilder gff3Builder = new GFF3FileBuilder(gff3File
					.isSorted());
			for (GFF3ElementGroup g : genes) {

				// expand range for fasta
				final Range r1 = g.getRange();
				final FASTAElement fe = fastaFile.getElementByHeader(g
						.getElements().iterator().next().getSeqID());
				debug("range for this group=" + r1);
				final AbstractSequence original = fe.getSequence();
				final Range r2 = r1.expand(OFFSET, original.getLength());
				debug("expanded range=" + r2);
				final AbstractSequence expanded = original.getSubSequence(r2
						.toZeroBasedInclusive());
				debug("expanded seq=" + expanded);
				// System.err.println(r1.getStart()-OFFSET);
				final Range r3 = r1.shiftRange(-(r1.getStart() - OFFSET))
						.toOneBasedExclusive();
				debug("shifted range=" + r3);

				if (fastaBuilder.containsElement(new FASTAElementImpl(fe
						.getHeader(), expanded))) {
					info("skipping fasta element");
				} else {
					fastaBuilder.addElement(new FASTAElementImpl(
							fe.getHeader(), expanded));
				}

				debug("old group range=" + g.getRange());

				// plus 1 is dirty and i don't know why!!
				g.shiftRange(-(r1.getStart() - OFFSET) + 1);
				debug("new group range=" + g.getRange());
				gff3Builder.addAllElements(g);

			}

			gff3Builder.build().write(f4);
			fastaBuilder.build().write(f2);

			// integrity check //
			
			info("starting integrity test");
			
			int fail = 0;
			int success = 0;
			
			for (GFF3ElementGroup g : genes) {
				final GFF3ElementGroup gene = g
						.getElements(new GFF3ElementTypeFilter(Type.gene));
				if (gene.getSize() != 1) {
					warn("fak");
				}
				final GFF3Element e = gene.iterator().next();
				final FASTAElement fe = fastaBuilder.build()
						.getElementByHeader(e.getSeqID());

				final GFF3ElementGroup gg = gff3File.getElements()
						.getElementsByAttributeValueIgnoreCase(
								e.getAttributes().getAttributeByKeyIgnoreCase(
										"id").getValue());
				if (gene.getSize() != 1) {
					warn("fak");
				}
				final GFF3Element e2 = gg.iterator().next();
				final FASTAElement fe2 = fastaFile.getElementByHeader(e2
						.getSeqID());

				try{
				final AbstractSequence olds = fe2.getSequence().getSubSequence(
						e2.getRange());
				final AbstractSequence news = fe.getSequence().getSubSequence(
						e.getRange());

				if (olds.equals(news)) {
					success++;
				} else {
					warn("elements do not match");
					warn("new\t" + e);
					warn("old\t" + e2);
					warn("new\t" + news);
					warn("old\t" + olds);
					fail++;
				}
				
				} catch(Exception ex){
					ex.printStackTrace();
					warn("new\t" + e);
					warn("old\t" + e2);
//					warn("new\t" + news);
//					warn("old\t" + olds);
					fail++;
				}

			}
			info("integrity test done (success="+success + ", fail="+fail+").");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
