package de.fh.giessen.ringversuch.view;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;

public class ViewUtils {
	
	private final static ExecutorService exe = Executors.newCachedThreadPool();
//	private final static Logger LOGGER = Logger.getLogger(ViewUtils.class);
	
	private ViewUtils(){}
	
	public static void dropToEventThread(Runnable runnable){
		javax.swing.SwingUtilities.invokeLater(runnable);
	}
	
	public static void escapeFromEventThread(Callable<Void> callable){
		exe.submit(callable);
	}
	
	public static <T> T escapeFromEventThread(Callable<T> callable) throws InterruptedException, ExecutionException{
		return exe.submit(callable).get();
	}
	
	public static void shutdown(){
		exe.shutdown();
	}
}
