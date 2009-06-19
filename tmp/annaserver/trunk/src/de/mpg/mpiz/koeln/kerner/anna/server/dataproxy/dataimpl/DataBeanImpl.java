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
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

/**
 * 
 * @Threadsave
 * 
 */
@SuppressWarnings("unchecked")
public class DataBeanImpl implements DataBean {

	private static final long serialVersionUID = 2776955959983685805L;
	private ArrayList<FASTASequence> verifiedGenesFastas = new ArrayList<FASTASequence>();
	private ArrayList<GTFElement> verifiedGenesGTFs = new ArrayList<GTFElement>();
	private ArrayList<GTFElement> predictedGenesGTFs = new ArrayList<GTFElement>();
	private File conradTrainingFile = null;

	public synchronized void setVerifiedGenesFasta (
			ArrayList<? extends FASTASequence> sequences)
			throws DataBeanAccessException {
		if (sequences == null)
			throw new NullPointerException();
		if (sequences.size() == 0)
			return;
		try {
			this.verifiedGenesFastas.clear();
			this.verifiedGenesFastas
					.addAll(deepCopy(ArrayList.class, sequences));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized void setVerifiedGenesGtf (
			ArrayList<? extends GTFElement> el) throws DataBeanAccessException {
		if (el == null)
			throw new NullPointerException();
		if (el.size() == 0)
			return;
		try {
			this.verifiedGenesGTFs.clear();
			this.verifiedGenesGTFs.addAll(deepCopy(ArrayList.class, el));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized ArrayList<? extends FASTASequence> getVerifiedGenesFasta()
			throws DataBeanAccessException {
		try {
			return new ArrayList<FASTASequence>(deepCopy(ArrayList.class,
					verifiedGenesFastas));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized ArrayList<? extends GTFElement> getVerifiedGenesGtf()
			throws DataBeanAccessException {
		try {
			return new ArrayList<GTFElement>(deepCopy(ArrayList.class,
					verifiedGenesGTFs));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized File getConradTrainingFile() {
		if (conradTrainingFile == null)
			return null;
		return new File(conradTrainingFile.getAbsolutePath());
	}

	public synchronized void setConradTrainingFile(File file)
			throws DataBeanAccessException {
		if (file == null || !file.exists() || !file.canRead())
			throw new DataBeanAccessException("conrad training file invalid ("
					+ file + ")");
		this.conradTrainingFile = new File(file.getAbsolutePath());
	}

	public synchronized ArrayList<? extends GTFElement> getPredictedGenesGtf()
			throws DataBeanAccessException {
		try {
			return new ArrayList<GTFElement>(deepCopy(ArrayList.class,
					predictedGenesGTFs));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized void setPredictedGenesGtf(
			ArrayList<? extends GTFElement> elements)
			throws DataBeanAccessException {
		if (elements == null)
			throw new NullPointerException();
		if (elements.size() == 0)
			return;
		try {
			elements.clear();
			this.predictedGenesGTFs.addAll(deepCopy(ArrayList.class, elements));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataBean:");
		sb.append(FileUtils.NEW_LINE);
		sb.append("FASTAs:");
		sb.append(FileUtils.NEW_LINE);
		if (verifiedGenesFastas.size() != 0) {
			for (FASTASequence seq : verifiedGenesFastas) {
				sb.append(seq);
			}
		}
		sb.append("GTFs:");
		sb.append(FileUtils.NEW_LINE);
		if (verifiedGenesGTFs.size() != 0)
			for (GTFElement e : verifiedGenesGTFs) {
				sb.append(e);
				sb.append(FileUtils.NEW_LINE);
			}
		return sb.toString();
	}

	/**
	 * "STOLEN" from kerner commons, du to class loader issues
	 * 
	 * @param <V>
	 * @param c
	 * @param s
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static <V> V deepCopy(Class<V> c, Serializable s) throws IOException,
			ClassNotFoundException {
		if (c == null || s == null)
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
