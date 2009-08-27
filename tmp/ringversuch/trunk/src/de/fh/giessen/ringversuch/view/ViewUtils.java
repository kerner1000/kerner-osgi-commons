package de.fh.giessen.ringversuch.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewUtils {
	
	private final static ExecutorService exe = Executors.newCachedThreadPool();
//	private final static Logger LOGGER = Logger.getLogger(ViewUtils.class);
	
	private ViewUtils(){}
	
	public static void dropToEventThread(Runnable runnable){
		javax.swing.SwingUtilities.invokeLater(runnable);
	}
	
	public static void escapeFromEventThread(Runnable runnable){
		exe.submit(runnable);
	}
	
	public static void shutdown(){
		exe.shutdown();
	}
}
