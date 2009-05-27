package de.mpg.mpizkoeln.kerner.anna.core;

import java.util.Collection;

import org.bioutils.Utils;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

public class DataBean {

	private Collection<FASTASequence> sequences = null;

	private Collection<GTFElement> gtfs = null;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("FASTA-Sequences: ");
		sb.append(Utils.NEW_LINE);
		if (sequences == null) {
			sb.append(sequences.toString());
		} else {
			for (FASTASequence seq : sequences) {
				sb.append(seq.toString());
				sb.append(Utils.NEW_LINE);
			}
		}
		if (gtfs == null) {
			sb.append(gtfs.toString());
		} else {
			sb.append("GTF-Elements: ");
			sb.append(Utils.NEW_LINE);
			for (GTFElement e : gtfs) {
				sb.append(e);
				sb.append(Utils.NEW_LINE);
			}
		}
		return sb.toString();
	}

	public DataBean() {

	}

	public Collection<FASTASequence> getValidatedFASTASeqs() {
		return sequences;
	}

	public Collection<GTFElement> getValidatedGTFs() {
		return gtfs;
	}

	public void setValidatedFASTASeqs(Collection<FASTASequence> sequences) {
		this.sequences = sequences;
	}

	public void setValidatedGTFs(Collection<GTFElement> gtfs) {
		this.gtfs = gtfs;
	}

}
