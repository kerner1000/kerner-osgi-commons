package de.fh.giessen.ringversuch.view;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewUtils {
	
	private final static ExecutorService exe = Executors.newCachedThreadPool();
//	private final static Logger LOGGER = Logger.getLogger(ViewUtils.class);
	
	private ViewUtils(){}
	
	public static void dropToEventThread(Runnable runnable){
		javax.swing.SwingUtilities.invokeLater(runnable);
	}
	
	/**
	 * Will not block
	 * 
	 * @param callable
	 */
	public static void escapeFromEventThread(Callable<Void> callable){
		exe.submit(callable);
	}
	
	/**
	 * Will block
	 * 
	 * @param <T>
	 * @param callable
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static <T> T escapeFromEventThread(Callable<T> callable) throws InterruptedException, ExecutionException{
		return exe.submit(callable).get();
	}
	
	public static void shutdown(){
		exe.shutdown();
	}
}
