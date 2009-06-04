package de.mpg.mpiz.koeln.kerner.dataproxy;

import java.io.Serializable;
import java.util.ArrayList;

import org.bioutils.LazySequence;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.fasta.FASTASequenceImpl;
import org.bioutils.gtf.GTFElement;

import de.kerner.commons.file.FileUtils;

class DataBean implements Serializable {

	private static final long serialVersionUID = 2776955959983685805L;
	private ArrayList<FASTASequence> sequences = new ArrayList<FASTASequence>();
	private ArrayList<GTFElement> elements = new ArrayList<GTFElement>();

	@SuppressWarnings("unchecked")
	void addVerifiedGenesFasta(ArrayList<FASTASequence> sequences) {
		if (sequences == null)
			throw new NullPointerException();
		try {
			this.sequences.addAll(Utils.deepCopy(ArrayList.class, sequences));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	void addVerifiedGenesGtf(ArrayList<GTFElement> el) {
		if (el == null)
			throw new NullPointerException();
		try {
			this.elements.addAll(Utils.deepCopy(ArrayList.class, el));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	ArrayList<? extends FASTASequence> getVerifiedGenesFasta() {
		return new ArrayList<FASTASequence>(sequences);
	}

	ArrayList<? extends GTFElement> getVerifiedGenesGtf() {
		return new ArrayList<GTFElement>(elements);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataBean:");
		sb.append(FileUtils.NEW_LINE);
		sb.append("FASTAs:");
		sb.append(FileUtils.NEW_LINE);
		if (sequences.size() != 0) {
			System.err.println(sequences.getClass());
			System.err.println("++"+sequences.get(0)+"++");
			System.err.println(sequences.get(0).getClass());
			for (FASTASequence seq : sequences) {
				sb.append(seq);
				// sb.append(Utils.NEW_LINE);
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

	public static void main(String[] args) {

		DataBean data = new DataBean();
		FASTASequence seq = new FASTASequenceImpl("dumb header",
				new LazySequence("affe"));
		ArrayList<FASTASequence> list = new ArrayList<FASTASequence>();
		list.add(seq);
		data.addVerifiedGenesFasta(list);
		// FileUtils.objectToXML(data, file);
		// System.out.println(FileUtils.XMLToObject(DataBean.class, file));
		System.out.println(data);

	}
}
