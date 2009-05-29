package de.mpg.mpizkoeln.kerner.anna.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.mpg.mpizkoeln.kerner.anna.annaservice.impl.AnnaServiceImpl;

public class DataBeanProxy {

	private static class Hans implements Callable<Void>{
		private final DataBeanModifier m;
		Hans(DataBeanModifier m){
			this.m = m;
		}
		public Void call() throws Exception {
			DataBean data = getInstance().readFromDisk();
			m.modify(data);
			getInstance().writeToDisk(data);
			return null;
		}
	}
	
	private final File file;
	private static DataBeanProxy INSTANCE;
	private static final ExecutorService exe = Executors.newSingleThreadExecutor();
	
	private DataBeanProxy() throws IOException {
		DataBean data = new DataBean();
		this.file = File.createTempFile(data.getClass().getSimpleName(), ".xml", new File(
				"/home/pcb/kerner/Desktop"));
		writeToDisk(data);
	}

	private static DataBeanProxy getInstance() throws IOException {
		if (INSTANCE == null) {
			System.err.println("New DataBeanProxy instance created");
			// don't synchronize another time on "DataBeanProxy.class"
			// we already did that with "public static synchronized void set...
			
			//synchronized (DataBeanProxy.class) {
			//	if (INSTANCE == null) {
			
					INSTANCE = new DataBeanProxy();
					
			//	}
			//}
		}
		return INSTANCE;
	}

	private synchronized void writeToDisk(DataBean data) throws IOException {
		
		/**
		Throwable t = new Throwable();
		StackTraceElement[] elements = t.getStackTrace();
		String calleeMethod = elements[0].getMethodName();
		String callerMethodName = elements[1].getMethodName();
		String callerClassName = elements[1].getClassName();
		System.out.println("CallerClassName=" + callerClassName + " , Caller method name: " + callerMethodName);
		System.out.println("Callee method name: " + calleeMethod); 
		System.err.println("DATA:\n"+data);
		*/
		System.err.println("Going to write data:\n" + data);
		de.kerner.commons.file.Utils.objectToXML(data, file);
		System.err.println("Sucessfully wrote data: " + file);
	}

	private synchronized DataBean readFromDisk() throws DataBeanAccessException {
		try {
			System.err.println("Going to read data:\n" + file);
			DataBean data = de.kerner.commons.file.Utils.XMLToObject(DataBean.class, file);
			System.err.println("Sucessfully read data: " + data);
			return data;
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public static synchronized void setValidatedFASTASeqs(final ArrayList<FASTASequence> sequences) throws Exception {
		DataBeanModifier m = new DataBeanModifier(){
			public void modify(DataBean data) {
				data.setValidatedFASTASeqs(sequences);
			}
		};
		exe.submit(new Hans(m)).get();
	}

	public static synchronized void setValidatedGTFs(final ArrayList<GTFElement> elements) throws Exception {
		DataBeanModifier m = new DataBeanModifier(){
			public void modify(DataBean data) {
				data.setValidatedGTFs(elements);
			}
		};
		//exe.submit(new Hans(m)).get();
	}
}
