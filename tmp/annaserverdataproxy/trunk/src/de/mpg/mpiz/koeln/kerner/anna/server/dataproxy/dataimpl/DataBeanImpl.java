package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.dataimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;

@SuppressWarnings("unchecked")
public class DataBeanImpl implements DataBean {

	private static final long serialVersionUID = 2776955959983685805L;
	private ArrayList<FASTASequence> sequences = new ArrayList<FASTASequence>();
	private ArrayList<GTFElement> elements = new ArrayList<GTFElement>();
	private File conradTrainingFile = null;

	public synchronized void setVerifiedGenesFasta(
			ArrayList<? extends FASTASequence> sequences) throws Exception {
		if (sequences == null)
			throw new NullPointerException();
		this.sequences.addAll(deepCopy(ArrayList.class, sequences));
	}

	public synchronized void setVerifiedGenesGtf(
			ArrayList<? extends GTFElement> el) throws Exception {
		if (el == null)
			throw new NullPointerException();
		this.elements.addAll(deepCopy(ArrayList.class, el));
	}

	public synchronized ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws Exception {
		return new ArrayList<FASTASequence>(deepCopy(ArrayList.class,
				sequences));
	}

	public synchronized ArrayList<? extends GTFElement> getVerifiedGenesGtf()
			throws Exception {
		return new ArrayList<GTFElement>(deepCopy(ArrayList.class,
				elements));
	}
	
	public synchronized File getConradTrainingFile() {
		return new File(conradTrainingFile.getAbsolutePath());
	}

	public synchronized void setConradTrainingFile(File file) {
			this.conradTrainingFile = new File(file.getAbsolutePath());	
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

	/**
	 * "STOLEN" from kerner commons, du to class loader issues
	 * @param <V>
	 * @param c
	 * @param s
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <V> V deepCopy(Class<V> c, Serializable s) throws IOException, ClassNotFoundException{
        if(c == null || s == null)
                throw new NullPointerException();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        new ObjectOutputStream(bs).writeObject(s);
        ByteArrayInputStream bi = new ByteArrayInputStream(bs.toByteArray());
        V v = c.cast(new ObjectInputStream(bi).readObject());
        bs.close();
        bi.close();
        return v;
}
	
}
