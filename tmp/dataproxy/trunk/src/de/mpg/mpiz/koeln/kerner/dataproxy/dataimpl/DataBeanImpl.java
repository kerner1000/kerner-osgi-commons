package de.mpg.mpiz.koeln.kerner.dataproxy.dataimpl;

import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.kerner.commons.Utils;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBean;

@SuppressWarnings("unchecked")
public class DataBeanImpl implements DataBean {

	private static final long serialVersionUID = 2776955959983685805L;
	private ArrayList<FASTASequence> sequences = new ArrayList<FASTASequence>();
	private ArrayList<GTFElement> elements = new ArrayList<GTFElement>();

	public synchronized void setVerifiedGenesFasta(
			ArrayList<? extends FASTASequence> sequences) throws Exception {
		if (sequences == null)
			throw new NullPointerException();
		this.sequences.addAll(Utils.deepCopy(ArrayList.class, sequences));
	}

	public synchronized void setVerifiedGenesGtf(
			ArrayList<? extends GTFElement> el) throws Exception {
		if (el == null)
			throw new NullPointerException();
		this.elements.addAll(Utils.deepCopy(ArrayList.class, el));
	}

	public synchronized ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws Exception {
		return new ArrayList<FASTASequence>(Utils.deepCopy(ArrayList.class,
				sequences));
	}

	public synchronized ArrayList<? extends GTFElement> getVerifiedGenesGtf()
			throws Exception {
		return new ArrayList<GTFElement>(Utils.deepCopy(ArrayList.class,
				elements));
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
