package de.mpg.mpizkoeln.kerner.anna.core;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import org.bioutils.Utils;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;
import org.bioutils.gtf.GTFFile;
import org.bioutils.gtf.GTFFormatErrorException;

public class DataBean implements Serializable {

	private static final long serialVersionUID = -7937932290254682681L;

	private Collection<FASTASequence> sequences = null;

	private Collection<GTFElement> gtfs = null;

	DataBean() {

	}
	
	Collection<FASTASequence> getValidatedFASTASeqs() {
		return sequences;
	}

	Collection<GTFElement> getValidatedGTFs() {
		return gtfs;
	}

	void setValidatedFASTASeqs(Collection<FASTASequence> sequences) {
		this.sequences = sequences;
	}

	void setValidatedGTFs(Collection<GTFElement> gtfs) {
		this.gtfs = gtfs;
	}
	
	private void writeObject(ObjectOutputStream s) throws IOException{
		s.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException{
		s.defaultReadObject();
	}
	
	public static void main(String[] args) {

		try {
			File s = new File("/home/pcb/kerner/Desktop/out.ser");
			de.kerner.commons.file.Utils.objectToFile(new DataBean(), s);
			System.out.println(de.kerner.commons.file.Utils.fileToObject(
					DataBean.class, s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("FASTA-Sequences: ");
		sb.append(Utils.NEW_LINE);
		if (sequences == null) {
			sb.append("null");
			sb.append(Utils.NEW_LINE);
		} else {
			for (FASTASequence seq : sequences) {
				sb.append(seq.toString());
				sb.append(Utils.NEW_LINE);
			}
		}
		sb.append("GTF-Elements: ");
		sb.append(Utils.NEW_LINE);
		if (gtfs == null) {
			sb.append("null");
			sb.append(Utils.NEW_LINE);
		} else {
			for (GTFElement e : gtfs) {
				sb.append(e);
				sb.append(Utils.NEW_LINE);
			}
		}
		return sb.toString();
	}

}
