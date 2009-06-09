package de.mpg.mpiz.koeln.kerner.dataproxy;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.bioutils.LazySequence;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.fasta.FASTASequenceImpl;
import org.bioutils.gtf.GTFElement;

import de.kerner.commons.file.FileUtils;

class DataBean implements Serializable {

	private static final long serialVersionUID = 2776955959983685805L;
	private ArrayList<FASTASequence> sequences = null;
	private ArrayList<GTFElement> elements = null;

	//@SuppressWarnings("unchecked")
	void setVerifiedGenesFasta(ArrayList<? extends FASTASequence> sequences) {
		if (sequences == null)
			throw new NullPointerException();
		//try {
			this.sequences = new ArrayList<FASTASequence>();
			//this.sequences.addAll(Utils.deepCopy(ArrayList.class, sequences));
		//} catch (Exception e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
			
			this.sequences.addAll(sequences);
	}

	@SuppressWarnings("unchecked")
	void setVerifiedGenesGtf(ArrayList<? extends GTFElement> el) {
		if (el == null)
			throw new NullPointerException();
//		try {
			this.elements = new ArrayList<GTFElement>();
//			this.elements.addAll(Utils.deepCopy(ArrayList.class, el));
//		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
			this.elements.addAll(el);
	}

//	@SuppressWarnings("unchecked")
	ArrayList<? extends FASTASequence> getVerifiedGenesFasta() {
//		ArrayList<FASTASequence> list = null;
//		try {
//			list = new ArrayList<FASTASequence>(Utils.deepCopy(ArrayList.class, sequences));
//		} catch (IOException e) {
//			e.printStackTrace();
//			list = sequences;
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			list = sequences;
//		}
//		return list;
		
		return new ArrayList<FASTASequence>(sequences);
	}

//	@SuppressWarnings("unchecked")
	ArrayList<? extends GTFElement> getVerifiedGenesGtf() {
//		ArrayList<GTFElement> list = null;
//		try {
//			list = new ArrayList<GTFElement>(Utils.deepCopy(ArrayList.class, elements));
//		} catch (IOException e) {
//			e.printStackTrace();
//			list = elements;
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			list = elements;
//		}
//		return list;
		
		return new ArrayList<GTFElement>(elements);
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
		data.setVerifiedGenesFasta(list);
		// FileUtils.objectToXML(data, file);
		// System.out.println(FileUtils.XMLToObject(DataBean.class, file));
		System.out.println(data);

	}
}
