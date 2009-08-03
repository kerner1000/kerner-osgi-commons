package de.mpg.mpiz.koeln.anna.server.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import de.bioutils.fasta.FASTASequence;
import de.bioutils.gff.GFFElement;
import de.bioutils.gtf.GTFElement;

public interface DataBean extends Serializable {

	public void setVerifiedGenesFasta(
			ArrayList<? extends FASTASequence> sequences)
			throws DataBeanAccessException;

	public ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws DataBeanAccessException;

	public ArrayList<? extends GTFElement> getVerifiedGenesGtf()
			throws DataBeanAccessException;

	public void setVerifiedGenesGtf(ArrayList<? extends GTFElement> elements)
			throws DataBeanAccessException;

	public File getConradTrainingFile() throws DataBeanAccessException;

	public void setConradTrainingFile(File file) throws DataBeanAccessException;

	public ArrayList<? extends GTFElement> getPredictedGenesGtf()
			throws DataBeanAccessException;

	public void setPredictedGenesGtf(ArrayList<? extends GTFElement> elements)
			throws DataBeanAccessException;

	public void setInputSequences(ArrayList<? extends FASTASequence> sequences)
			throws DataBeanAccessException;

	public ArrayList<? extends FASTASequence> getInputSequences()
			throws DataBeanAccessException;

	public ArrayList<? extends GFFElement> getRepeatMaskerGff()
			throws DataBeanAccessException;

	public void setRepeatMaskerGff(ArrayList<? extends GFFElement> elements)
			throws DataBeanAccessException;

}
