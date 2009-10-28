import java.io.File;
import java.io.IOException;

import de.bioutils.AbstractSequence;
import de.bioutils.Range;
import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.FASTAElementImpl;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;

public class Processor {

	private final static File f1 = new File(
			"/home/pcb/kerner/Desktop/test.fasta");
	private final static File f2 = new File(
			"/home/pcb/kerner/Desktop/new.fasta");

	private final static File f3 = new File("/home/pcb/kerner/Desktop/test.gff");
	private final static File f4 = new File("/home/pcb/kerner/Desktop/new.gff");

	private final static int OFFSET = 200;
	private final static int LENGTH_THRESH = 100;

	public static void main(String[] args) {
		try {
			final NewFASTAFile fastaFile = NewFASTAFileImpl.parse(f1);
			final GFF3File gff3File = GFF3FileImpl.convertFromGFF(f3);
			final FASTAFileBuilder fastaBuilder = new FASTAFileBuilder();
			final GFF3FileBuilder gffBuilder = new GFF3FileBuilder();
			for (GFF3Element e : gff3File.getElements()) {
				final String fastaHeader = e.getSeqID();
				final FASTAElement fastaElement = fastaFile
						.getElementByHeader(fastaHeader);
				final int seqLength = fastaElement.getSequence().getLength();
				if (seqLength <= LENGTH_THRESH) {
					// current fasta sequence is not out of bounds, for this
					// sequence and also for this gff element, we do not need to
					// change anything.
					fastaBuilder.addElement(fastaElement);
					gffBuilder.addElement(e);
					continue;
				}
				// current fasta sequence is out of bounds, we need to cut
				// apropiate sequence and edit also the gff element.
				
				final int newStop = getNewStop(e, fastaElement);
				final int newStart = getNewStart(e, fastaElement);
				final AbstractSequence newFastaSeq = fastaElement.getSequence()
						.getSubSequence(new Range(newStart, newStop));
				fastaBuilder.addElement(new FASTAElementImpl(fastaElement
						.getHeader(), newFastaSeq));
				gffBuilder.addElement(e);
			}
			fastaBuilder.build().write(f2);
			gffBuilder.build().write(f4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int getNewStart(GFF3Element e, FASTAElement fastaElement) {
		final int oldStart = e.getStart();

		// normally, new start equals offset
		int result = OFFSET;

		// if gene starts somewhere at the beginning of the sequence, we don't
		// change anything.
		if (oldStart < OFFSET)
			result = oldStart;

		return result;
	}

	private static int getNewStop(GFF3Element e, FASTAElement fastaElement) {
		// final int oldStop = e.getStop();
		final int sequenceLength = fastaElement.getSequence().getLength();

		// normally, new stop equals offset + gene length
		int result = OFFSET + new Range(e.getStart(), e.getStop()).getLength();

		// if gene stops somewhere at the end of the sequence, we take just as
		// much offset as we can get.
		if (result > sequenceLength)
			result = sequenceLength;

		return result;
	}

}
