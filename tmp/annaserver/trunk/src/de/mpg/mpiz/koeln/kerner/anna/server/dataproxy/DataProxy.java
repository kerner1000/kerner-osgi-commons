package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

public interface DataProxy {

	void setVerifiedGenesGtf(ArrayList<? extends GTFElement> elements) throws DataBeanAccessException;

	void setVerifiedGenesFasta(ArrayList<? extends FASTASequence> sequences) throws DataBeanAccessException;

	ArrayList<? extends FASTASequence> getVerifiedGenesFasta() throws DataBeanAccessException;

	ArrayList<? extends GTFElement> getVerifiedGenesGtf() throws DataBeanAccessException;

	ArrayList<? extends FASTASequence> getInputSequences() throws DataBeanAccessException;

	void setInputSequences(ArrayList<? extends FASTASequence> fastas) throws DataBeanAccessException;

	File getConradTrainingFile() throws DataBeanAccessException;

	void setConradTrainingFile(File trainingFile) throws DataBeanAccessException;

	ArrayList<? extends GTFElement> getPredictedGenesGtf() throws DataBeanAccessException;

	void setPredictedGenesGtf(ArrayList<? extends GTFElement> result) throws DataBeanAccessException;
	
	ArrayList<? extends GTFElement> getRepeatMaskerGtf()
	throws DataBeanAccessException;

	void setRepeatMaskerGtf(ArrayList<? extends GTFElement> elements)
	throws DataBeanAccessException;
	
}
