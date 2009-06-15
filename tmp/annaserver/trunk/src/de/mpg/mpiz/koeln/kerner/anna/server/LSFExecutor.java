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
import java.util.ArrayList;
import java.util.List;

import de.kerner.commons.file.LazyStringReader;
import de.kerner.commons.other.CommandStringBuilder;
import de.kerner.commons.zip.ZipUtils;
import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.utils.AnnaUtils;

class LSFExecutor extends AbstractStepExecutor {

	private final static File JAR_FILE = new File("/home/pcb/kerner/Desktop/"
			+ "aeffchen" + ".jar");
	private final static File SER_FILE_PATH = new File(
			"/home/pcb/kerner/Desktop/");
	private final File fileName = new File(SER_FILE_PATH, "number.ser");

	private List<String> getCommandList() {
		final CommandStringBuilder b = new CommandStringBuilder("bsub");
		b.addFlagCommand("-K");
		b.addValueCommand("-m", "pcbcomputenodes");
		b.addValueCommand("-eo", "/home/pcb/kerner/Desktop/java.err");
		b.addValueCommand("-oo", "/home/pcb/kerner/Desktop/java.out");
		b.addValueCommand("java", "-jar");
		b.addFlagCommand(JAR_FILE.getAbsolutePath());
		b.addFlagCommand(fileName.getAbsolutePath());
		return b.getCommandList();
	}

	LSFExecutor(AbstractStep step, ServerActivator serverActivator) {
		super(step, serverActivator);
		try {
			
		final Class<? extends AbstractStep> clazz = step.getClass();
		final File jarFile = new File(clazz.getProtectionDomain()
				.getCodeSource().getLocation().toURI().getPath());
		final List<File> list = new ArrayList<File>(){
			{
				add(new File("/home/pcb/kerner/Desktop/pipelinetest/plugins/de.mpg.mpiz.koeln.kerner.anna.server_0.0.2.jar"));
			}
		};
		AnnaUtils.ConvertBundleToRunnableJar(jarFile, JAR_FILE, list);
		ServerActivator.LOGGER.debug(this, "creating serialized databean " + fileName);
		objectToFile(super.serverActivator.getDataProxy().getDataBean(), fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean call() throws Exception {
		final List<String> list = getCommandList();
		final ProcessBuilder builder = new ProcessBuilder(list);
		final Process p = builder.start();
		ServerActivator.LOGGER
				.debug(this, "process created:" + p + ", " + list);
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

	private static <V> V fileToObject(Class<V> c, File file)
			throws IOException, ClassNotFoundException {
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
