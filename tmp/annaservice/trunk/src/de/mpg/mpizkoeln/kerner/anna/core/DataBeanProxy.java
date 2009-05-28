package de.mpg.mpizkoeln.kerner.anna.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

public class DataBeanProxy {

	private static class Hans implements Callable<Void>{
		private final DataBeanModifier m;
		Hans(DataBeanModifier m){
			this.m = m;
		}
		public Void call() throws Exception {
			DataBean data = getInstance().deSerialize();
			m.modify(data);
			getInstance().serialize(data);
			return null;
		}
	}
	
	private final File file;
	private static DataBeanProxy INSTANCE;
	private static final ExecutorService exe = Executors.newSingleThreadExecutor();
	private DataBeanProxy() throws IOException {
		this.file = File.createTempFile("out", ".ser", new File(
				"/home/pcb/kerner/Desktop"));
		serialize(new DataBean());
	}

	private static DataBeanProxy getInstance() throws IOException {
		if (INSTANCE == null) {
			
			// don't synchronize another time on "DataBeanProxy.class"
			// we already did that with "public synchronized void set...
			
			//synchronized (DataBeanProxy.class) {
			//	if (INSTANCE == null) {
			
					INSTANCE = new DataBeanProxy();
					
			//	}
			//}
		}
		return INSTANCE;
	}

	private void serialize(DataBean data) throws IOException {
		de.kerner.commons.file.Utils.objectToFile(data, file);
	}

	private DataBean deSerialize() throws DataBeanAccessException {
		try {
			return de.kerner.commons.file.Utils.fileToObject(DataBean.class, file);
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public static synchronized void setValidatedFASTASeqs(final Collection<FASTASequence> sequences) throws Exception {
		DataBeanModifier m = new DataBeanModifier(){
			public void modify(DataBean data) {
				data.setValidatedFASTASeqs(sequences);
			}
		};
		exe.submit(new Hans(m)).get();
	}

	public static synchronized void setValidatedGTFs(final Collection<GTFElement> elements) throws Exception {
		DataBeanModifier m = new DataBeanModifier(){
			public void modify(DataBean data) {
				data.setValidatedGTFs(elements);
			}
		};
		exe.submit(new Hans(m)).get();
	}
}
