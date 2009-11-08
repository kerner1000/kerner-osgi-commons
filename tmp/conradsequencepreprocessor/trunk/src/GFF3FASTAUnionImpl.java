import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bioutils.AbstractSequence;
import de.bioutils.Utils;
import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.FASTAElementGroup;
import de.bioutils.fasta.FASTAElementImpl;
import de.bioutils.fasta.FASTAFileBuilder;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.gff3.GFF3FASTAUnion;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.IntegrityCheckException;
import de.bioutils.gff3.Type;
import de.bioutils.gff3.attribute.IDAttribute;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileBuilder;
import de.bioutils.range.Range;

public class GFF3FASTAUnionImpl implements GFF3FASTAUnion {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GFF3FASTAUnionImpl.class);

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

	private final GFF3File gff3;
	private final NewFASTAFile fasta;
	private final GFF3FASTAUnion parent;

	// Private //

	private void checkIDs() throws IntegrityCheckException {
		for (GFF3Element e : gff3.getElements()) {
			if (fasta.getElements().hasElementByHeader(e.getSeqID())) {
				// all good
			} else {
				throw new IntegrityCheckException("GFF3 sequence ID \""
						+ e.getSeqID() + "\" does not match any FASTA header");
			}
		}
	}
	
	private void fastaHeadersUnique() throws IntegrityCheckException {
		// use hashset to have optimal performance (we do not iterate but only look up)
		final Set<String> foundHeaders = new HashSet<String>();
		for(FASTAElement f : fasta.getElements()){
			final String header = f.getHeader();
			if(foundHeaders.contains(header)){
				throw new IntegrityCheckException("fasta headers are not unique");
			}
			foundHeaders.add(header);
		}
	}


	private void checkSequenceReference() throws IntegrityCheckException {
		info("comparing sequence references with those from parent");
		// we can only check elements, that have id string, because other elements we cannot distinguish!
		final GFF3ElementGroup gff3Parent = parent.getGFF3ElementGroup().getElements(new GFF3ElementHasIDAttributeIgnoreCaseFilter());
		final FASTAElementGroup fastaParent = parent.getFASTAElements();
		
		for (GFF3Element e : gff3.getElements().getElements(new GFF3ElementHasIDAttributeIgnoreCaseFilter())) {
			// get sequence that is currently refered to by element
			final AbstractSequence seq1 = fasta
					.getElementByHeader(e.getSeqID()).getSequence();
			// get sequence that is refered to by this element in parent
			final String idString = e.getAttributes()
					.getAttributeByKeyIgnoreCase("id").getValue();
			final GFF3ElementGroup groupParent = gff3Parent.getElements()
					.getElementsByAttribute(new IDAttribute(idString));
			if (groupParent.getSize() != 1) {
				warn("found more than one element with id \"" + idString + ")");
				warn(groupParent);
			}
			final GFF3Element eParent = groupParent.iterator().next();
			final AbstractSequence seq2 = fastaParent.getElementByHeader(
					eParent.getSeqID()).getSequence();
			
			debug("seq1="+seq1);
			debug("seq2="+seq2);

			// ranges 
			final Range r1 = e.getRange();
			final Range rParent = eParent.getRange();
//			
//			final Range r1 = new Range(0, seq1.getLength());
//			final Range rParent = new Range(0, seq2.getLength());
			
			final AbstractSequence seq11 = seq1.getSubSequence(r1);
			final AbstractSequence seq22 = seq2.getSubSequence(rParent);
			
			debug("seq11="+seq11);
			debug("seq22="+seq22);
			
			if (seq11.equals(seq22)) {
				// all good
			} else {
				throw new IntegrityCheckException(
						"sequence references do not match:" + Utils.NEW_LINE
						+ "element1=" + eParent + Utils.NEW_LINE
						+ "element1=" + seq22 + Utils.NEW_LINE
								+ "element2=" + e + Utils.NEW_LINE
								+ "element2=" + seq11 + Utils.NEW_LINE
				);
			}
		}

	}

	// Constructor //

	public GFF3FASTAUnionImpl(GFF3File gff3File, NewFASTAFile fastaFile) {
		this.gff3 = gff3File;
		this.fasta = fastaFile;
		this.parent = null;
	}

	// this one is private, so only a parent can create its childs.
	private GFF3FASTAUnionImpl(GFF3File gff3File, NewFASTAFile fastaFile,
			GFF3FASTAUnion parent) {
		this.gff3 = gff3File;
		this.fasta = fastaFile;
		this.parent = parent;
	}

	// Implement //

	public FASTAElementGroup getFASTAElements() {
		return fasta.getElements();
	}

	public GFF3ElementGroup getGFF3ElementGroup() {
		return gff3.getElements();
	}

	public void checkIntegrity() throws IntegrityCheckException {
		// check if all gff3Element have sequence ID that matches one fasta
		// element header
		checkIDs();
		
		// check wether all fasta headers are uniuqe
		fastaHeadersUnique();

		// if backups are non-null, we can also check if changes we made where
		// correct.
		// we do that by directly comparing sequences gff3 elements refer to.
		if (parent == null) {
			// skip
		} else {
			checkSequenceReference();
		}
	}

	public GFF3File getMaster() {
		return gff3;
	}

	public NewFASTAFile getSlave() {
		return fasta;
	}

	public GFF3FASTAUnion trimmFastas(int offset) {
		// in oder to trimm fastas, first we must know which gff3 elements
		// belong together to find needed range.
		// by default we trimm to "Type.gene"
		return trimmFastas(offset, Type.gene);
	}

	private GFF3FASTAUnion trimmFastas(int offset, Type type) {
		// put all genes to one group.
		debug("grouping GFF3 elements to genes");
		final List<GFF3ElementGroup> genes = gff3.getElements()
				.getAllChildsForTypeList(type);
		debug("grouping GFF3 elements to genes done");
		debug(genes);
		

		final FASTAFileBuilder fastaBuilder = new FASTAFileBuilder();
		fastaBuilder.setLineLength(10);
		final GFF3FileBuilder gff3Builder = new GFF3FileBuilder(gff3.isSorted());
		final StringIncreaser si = new StringIncreaser();
		
		// now it begins!!...
		for (GFF3ElementGroup g : genes) {
			
			final boolean idConsistent = g.isConsistentBySequenceID();
			debug("check, wether group is consistent by sequence ID: " + idConsistent);
			if(idConsistent){
				// all good
			} else {
				warn("group is not consistend by ID!" + Utils.NEW_LINE + g);
			}
			
			// first find range for exact sequence
			final Range r1 = g.getRange();
			// coresponding fasta element
			final FASTAElement fe = fasta.getElementByHeader(g.getElements()
					.iterator().next().getSeqID());
			debug("- - - - - - " + Utils.NEW_LINE + "range for this group="
					+ r1);

			// now find new Range concerning offset
			// to do so, we expand original range with offset.
			final AbstractSequence original = fe.getSequence();
			final Range r2 = r1.expand(offset, original.getLength());
			debug("expanded range=" + r2);
			// "zeroBasesIncluse" necessary, because indices of abstract
			// sequence are sero based exlusive, GFF3 indices are one based
			// inclusive.
			final AbstractSequence expanded = original.getSubSequence(r2
					.toZeroBasedExclusive());
			// all done for fasta element!
			final FASTAElement feNew = new FASTAElementImpl(si.increase(fe.getHeader()),
					expanded);
			if (fastaBuilder.containsElement(feNew)) {
				info("skipping fasta element");
			} else {
				fastaBuilder.addElement(feNew);
			}

			// now we must correct gff3 indices
			debug("old group range=" + g.getRange());

			// in order to do that, we shift ranges for this element group
			// offset - 1 so we start with first element AFTER offset
			g.shiftRange(-(r1.getStart() - offset - 1));
			debug("new group range=" + g.getRange());

			debug("original sequence=" + original);
			debug("expanded sequence=" + expanded);
			debug("shifted  sequence="
					+ expanded.getSubSequence(r1
							.shiftRange(-(r1.getStart() - offset))));
			
			// we now must fix ID string to match new fasta header (had to be changed to be still unique);
			g.setSequenceID(feNew.getHeader());

			gff3Builder.addAllElements(g);
		}
		return new GFF3FASTAUnionImpl(gff3Builder.build(),
				fastaBuilder.build(), this);
	}

	public void write(File f4, File f2) throws IOException {
		gff3.write(f4);
		fasta.write(f2);
	}

}
