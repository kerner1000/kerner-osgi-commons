package de.mpg.mpiz.koeln.kerner.dataproxy;

import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

public interface DataProxy {

	void setVerifiedGenesGtf(ArrayList<? extends GTFElement> el) throws DataBeanAccessException;

	void setVerifiedGenesFasta(ArrayList<? extends FASTASequence> sequences) throws DataBeanAccessException;

	ArrayList<? extends FASTASequence> getVerifiedGenesFasta()throws DataBeanAccessException;
	
	ArrayList<? extends GTFElement> getVerifiedGenesGtf()throws DataBeanAccessException;


}
