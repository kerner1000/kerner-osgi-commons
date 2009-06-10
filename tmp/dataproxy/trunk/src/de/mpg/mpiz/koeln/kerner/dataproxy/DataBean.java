package de.mpg.mpiz.koeln.kerner.dataproxy;

import java.io.Serializable;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.kerner.commons.file.FileUtils;

@SuppressWarnings("unchecked")
public class DataBean implements Serializable {

	private static final long serialVersionUID = 2776955959983685805L;
	private ArrayList<FASTASequence> sequences = null;
	private ArrayList<GTFElement> elements = null;

	void setVerifiedGenesFasta(ArrayList<? extends FASTASequence> sequences)
			throws Exception {
		if (sequences == null)
			throw new NullPointerException();
		this.sequences.addAll(Utils.deepCopy(ArrayList.class, sequences));
	}

	void setVerifiedGenesGtf(ArrayList<? extends GTFElement> el)
			throws Exception {
		if (el == null)
			throw new NullPointerException();
		this.elements.addAll(Utils.deepCopy(ArrayList.class, el));
	}

	ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws Exception {
		return new ArrayList<FASTASequence>(Utils.deepCopy(ArrayList.class,
				sequences));

	}

	ArrayList<? extends GTFElement> getVerifiedGenesGtf() throws Exception {
		return new ArrayList<GTFElement>(Utils.deepCopy(ArrayList.class, elements));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataBean:");
		sb.append(FileUtils.NEW_LINE);
		sb.append("FASTAs:");
		sb.append(FileUtils.NEW_LINE);
		if (sequences.size() != 0) {
			for (FASTASequence seq : sequences) {
				sb.append(seq);
			}
		}
		sb.append("GTFs:");
		sb.append(FileUtils.NEW_LINE);
		if (elements.size() != 0)
			for (GTFElement e : elements) {
				sb.append(e);
				sb.append(FileUtils.NEW_LINE);
			}
		return sb.toString();
	}
}
