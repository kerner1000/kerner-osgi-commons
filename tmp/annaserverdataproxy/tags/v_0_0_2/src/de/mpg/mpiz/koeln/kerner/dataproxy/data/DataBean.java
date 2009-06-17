package de.mpg.mpiz.koeln.kerner.dataproxy.data;

import java.io.Serializable;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

public interface DataBean extends Serializable {

	public void setVerifiedGenesFasta(
			ArrayList<? extends FASTASequence> sequences) throws Exception;

	public void setVerifiedGenesGtf(ArrayList<? extends GTFElement> elements) throws Exception;

	public ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws Exception;
	
	public ArrayList<? extends GTFElement> getVerifiedGenesGtf() throws Exception;

}
