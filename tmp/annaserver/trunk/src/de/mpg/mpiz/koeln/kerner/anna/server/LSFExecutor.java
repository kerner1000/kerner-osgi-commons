package de.mpg.mpiz.koeln.kerner.anna.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import de.kerner.commons.file.LazyStringReader;
import de.kerner.commons.other.CommandStringBuilder;
import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;

class LSFExecutor extends AbstractStepExecutor {

	private final static File JAR_FILE = new File("/home/pcb/kerner/Desktop/"
			+ "aeffchen" + ".jar");
	private final static File SER_FILE_PATH = new File("/home/pcb/kerner/Desktop/");

	private static List<String> getCommandList() {
		final CommandStringBuilder b = new CommandStringBuilder("bsub");
		b.addFlagCommand("-K");
		b.addValueCommand("-m", "pcbcomputenodes");
		b.addValueCommand("java", "-jar");
		b.addFlagCommand(JAR_FILE.getAbsolutePath());
		return b.getCommandList();
	}

	LSFExecutor(AbstractStep step) {
		super(step);
		final Class<? extends AbstractStep> clazz = step.getClass();
		// JarBuilder.buildJar(clazz, JAR_FILE);
		try {
			final File result = new File(clazz.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath());
			System.out.println("location of class file:" + result);
			final File ser = new File(SER_FILE_PATH, "number.ser");
			objectToFile(step, ser);
			System.out.println(fileToObject(clazz, ser));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean call() throws Exception {
		final ProcessBuilder builder = new ProcessBuilder(getCommandList());
		final Process p = builder.start();
		ServerActivator.LOGGER.debug(this, "process created:" + p);
		final InputStream is = p.getInputStream();
		final InputStream os = p.getErrorStream();
		final LazyStringReader out = new LazyStringReader(is);
		final LazyStringReader err = new LazyStringReader(os);
		ServerActivator.LOGGER.debug(this, "process output:" + out.getString());
		ServerActivator.LOGGER.debug(this, "process errorout:"
				+ err.getString());
		final int exitValue = p.waitFor();
		is.close();
		os.close();
		ServerActivator.LOGGER.debug(this, "process exited with exitcode "
				+ exitValue);
		return true;
	}

	private static void objectToFile(Serializable s, File file)
			throws IOException {
		if (s == null || file == null)
			throw new NullPointerException(s + " + " + file
					+ " must not be null");
		OutputStream fos = new FileOutputStream(file);
		ObjectOutputStream outStream = new ObjectOutputStream(fos);
		outStream.writeObject(s);
		outStream.close();
		fos.close();
	}

	private static <V> V fileToObject(Class<V> c, File file) throws IOException,
			ClassNotFoundException {
		if (c == null || file == null)
			throw new NullPointerException(c + " + " + file
					+ " must not be null");
		InputStream fis = new FileInputStream(file);
		ObjectInputStream inStream = new ObjectInputStream(fis);
		V v = c.cast(inStream.readObject());
		inStream.close();
		fis.close();
		return v;
	}
}
