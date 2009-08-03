package de.mpg.mpiz.koeln.anna.server.dataImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import de.bioutils.fasta.FASTASequence;
import de.bioutils.gff.GFFElement;
import de.bioutils.gtf.GTFElement;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;

/**
 * 
 * @ThreadSave (everything protected by this)
 * 
 */
@SuppressWarnings("unchecked")
public class DataBeanImpl implements DataBean {

	private static final long serialVersionUID = 2776955959983685805L;
	private ArrayList<FASTASequence> verifiedGenesFastas = new ArrayList<FASTASequence>();
	private ArrayList<FASTASequence> inputSequences = new ArrayList<FASTASequence>();
	private ArrayList<GTFElement> verifiedGenesGFFs = new ArrayList<GTFElement>();
	private ArrayList<GTFElement> predictedGenesGFFs = new ArrayList<GTFElement>();
	private ArrayList<GFFElement> repeatMaskerGFF = new ArrayList<GFFElement>();
	private File conradTrainingFile = null;
//	private final LogDispatcher logger;

	public DataBeanImpl(LogDispatcher logger) {
//		if (logger == null)
//			this.logger = new ConsoleLogger();
//		else
//			this.logger = logger;
	}

	public DataBeanImpl() {
//		this.logger = new ConsoleLogger();
	}

	private <V> V deepCopy(Class<V> c, Serializable s) throws IOException,
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

	public synchronized void setInputSequences(
			ArrayList<? extends FASTASequence> sequences)
			throws DataBeanAccessException {
		if (sequences == null)
			throw new NullPointerException();
		if (sequences.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.inputSequences.clear();
			this.inputSequences.addAll(deepCopy(ArrayList.class, sequences));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized ArrayList<? extends FASTASequence> getInputSequences()
			throws DataBeanAccessException {
		try {
			return new ArrayList<FASTASequence>(deepCopy(ArrayList.class,
					inputSequences));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized void setVerifiedGenesFasta(
			ArrayList<? extends FASTASequence> sequences)
			throws DataBeanAccessException {
		if (sequences == null)
			throw new NullPointerException();
		if (sequences.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
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

	public synchronized void setVerifiedGenesGtf(
			ArrayList<? extends GTFElement> el) throws DataBeanAccessException {
		if (el == null)
			throw new NullPointerException();
		if (el.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.verifiedGenesGFFs.clear();
			this.verifiedGenesGFFs.addAll(deepCopy(ArrayList.class, el));
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
					verifiedGenesGFFs));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized File getConradTrainingFile() {
		if (conradTrainingFile == null) {
			return null;
		}
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
					predictedGenesGFFs));
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
		if (elements.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.predictedGenesGFFs.clear();
			this.predictedGenesGFFs.addAll(deepCopy(ArrayList.class, elements));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized ArrayList<? extends GFFElement> getRepeatMaskerGff()
			throws DataBeanAccessException {
		try {
			return new ArrayList<GFFElement>(deepCopy(ArrayList.class,
					repeatMaskerGFF));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized void setRepeatMaskerGff(
			ArrayList<? extends GFFElement> elements)
			throws DataBeanAccessException {
		if (elements == null)
			throw new NullPointerException();
		if (elements.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.repeatMaskerGFF.clear();
			this.repeatMaskerGFF.addAll(deepCopy(ArrayList.class, elements));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}

	}

	public String toString() {
		return this.getClass().getSimpleName()
				+ Integer.toHexString(this.hashCode());
	}
}
