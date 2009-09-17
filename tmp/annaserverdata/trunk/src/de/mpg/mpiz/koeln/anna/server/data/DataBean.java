package de.mpg.mpiz.koeln.anna.server.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import de.bioutils.gff.element.NewGFFElement;
import de.bioutils.gtf.element.GTFElement;
import de.bioutils.fasta.FASTAElement;

public interface DataBean extends Serializable {

	public void setVerifiedGenesFasta(
			ArrayList<? extends FASTAElement> sequences)
			throws DataBeanAccessException;

	public ArrayList<? extends FASTAElement> getVerifiedGenesFasta()
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

	public void setInputSequences(ArrayList<? extends FASTAElement> sequences)
			throws DataBeanAccessException;

	public ArrayList<? extends FASTAElement> getInputSequences()
			throws DataBeanAccessException;

	public ArrayList<? extends NewGFFElement> getRepeatMaskerGff()
			throws DataBeanAccessException;

	public void setRepeatMaskerGff(ArrayList<? extends NewGFFElement> elements)
			throws DataBeanAccessException;

}
