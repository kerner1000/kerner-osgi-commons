package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

public interface DataBean extends Serializable {

	public void setVerifiedGenesFasta(
			ArrayList<? extends FASTASequence> sequences) throws DataBeanAccessException;
	
	public ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
	throws DataBeanAccessException;

	public ArrayList<? extends GTFElement> getVerifiedGenesGtf() throws DataBeanAccessException;
	
	public void setVerifiedGenesGtf(ArrayList<? extends GTFElement> elements) throws DataBeanAccessException;
	
	public File getConradTrainingFile() throws DataBeanAccessException;
	
	public void setConradTrainingFile(File file) throws DataBeanAccessException;
	
	public ArrayList<? extends GTFElement> getPredictedGenesGtf() throws DataBeanAccessException;
	
	public void setPredictedGenesGtf(ArrayList<? extends GTFElement> elements) throws DataBeanAccessException;

}
