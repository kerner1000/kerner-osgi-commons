import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bioutils.AbstractSequence;
import de.bioutils.Range;
import de.bioutils.Utils;
import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.FASTAElementImpl;
import de.bioutils.fasta.FastaUtils;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;

public class Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
	
	private static void warn(Object msg){
		if(LOGGER.isWarnEnabled()){
			LOGGER.warn(msg.toString());
		}
	}
	
	private static void debug(Object msg){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(msg.toString());
		}
	}
	
	private final static File f1 = new File(
			"/home/pcb/kerner/Desktop/test.fasta");
	private final static File f2 = new File(
			"/home/pcb/kerner/Desktop/new.fasta");

	private final static File f3 = new File("/home/pcb/kerner/Desktop/test.gff");
	private final static File f4 = new File("/home/pcb/kerner/Desktop/new.gff");

	private final static int OFFSET = 200;
	private final static int LENGTH_THRESH = 10;

	public static void main(String[] args) {
		try {
			final NewFASTAFile fastaFile = NewFASTAFileImpl.parse(f1);
			final GFF3File gff3File = GFF3FileImpl.convertFromGFF(f3);
			final FASTAFileBuilder fastaBuilder = new FASTAFileBuilder();
			fastaBuilder.setLineLength(10);
			final GFF3FileBuilder gffBuilder = new GFF3FileBuilder();
			
			
			for (GFF3Element e : gff3File.getElements()) {
				// get this element's sequence
				final FASTAElement fastaEl = fastaFile.getElementByHeader(e.getSeqID());
				if(fastaEl == null){
					warn("could not find matching fasta sequence for element \"" + e + "\"");
					break;
				}
				
				// if this elements sequence length is below LENGTH_THRESH, nothing do to
				if(fastaEl.getSequence().getLength() <= LENGTH_THRESH){
					// add this sequence only to new file, if it is not already there
					if(fastaBuilder.containsElement(fastaEl)){
						continue;
					} else {
						fastaBuilder.addElement(fastaEl);
					}
					
					// this element sequence length is too long, we need to cut
				} else {
					// get range of gene:
					// that means, first find all GFFElements, that describe the same gene
					// Map<? extends GFF3Element, Collection<? extends GFF3Element>> genes = GFF3Utils.getAllForGene(fastaBuilder.build());
					
					Collection<? extends GFF3Element> parents = gff3File.getParents(e);
					if(parents.size() != 0){
					debug("parents for element " + e.getAttributeLine().getAttributeByKeyIgnoreCase("parent"));
					int cnt = 1;
					for(GFF3Element ff : parents){
						debug(cnt++ + "-parent="+ff.getAttributeLine().getAttributeByKeyIgnoreCase("id"));
					}
					}
				}
			}
			
			fastaBuilder.build().write(f2);
			//FastaUtils.splitSequences(fastaBuilder.build(), 20).write(f2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getAllForGene(NewFASTAFile build) {
		// TODO Auto-generated method stub
		
	}

	

}
