package de.mpg.mpizkoeln.kerner.anna.core;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.bioutils.Utils;
import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

public class DataBean implements Serializable {

	private static final long serialVersionUID = -7937932290254682681L;

	private ArrayList<FASTASequence> sequences = new ArrayList<FASTASequence>();

	private ArrayList<GTFElement> gtfs = new ArrayList<GTFElement>();

	DataBean() {

	}

	ArrayList<FASTASequence> getValidatedFASTASeqs() {
		return sequences;
	}

	ArrayList<GTFElement> getValidatedGTFs() {
		return gtfs;
	}

	void setValidatedFASTASeqs(ArrayList<FASTASequence> sequences) {
		this.sequences = sequences;
	}

	void setValidatedGTFs(ArrayList<GTFElement> gtfs) {
		this.gtfs = gtfs;
	}

	public static void main(String[] args) {

		try {
			DataBean data = new DataBean();
			File file = new File("/home/pcb/kerner/Desktop/", data.getClass().getSimpleName()+".xml");
			de.kerner.commons.file.Utils.objectToXML(data, file);
			DataBean data2 = de.kerner.commons.file.Utils.XMLToObject(DataBean.class, file);
			System.out.println(data2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FASTA-Sequences: ");
		sb.append(Utils.NEW_LINE);
		if(sequences.size() != 0){
		for (FASTASequence seq : sequences) {
			sb.append(seq.toString());
			sb.append(Utils.NEW_LINE);
		}}//else{System.err.println("sequences size is " + sequences.size());}
		sb.append("GTF-Elements: ");
		sb.append(Utils.NEW_LINE);
		if(gtfs.size() != 0) {
		for (GTFElement e : gtfs) {
			sb.append(e);
			sb.append(Utils.NEW_LINE);
		}}//else{System.err.println("gtfs size is " + gtfs.size());}
		return sb.toString();
	}
}
