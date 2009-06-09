package de.mpg.mpiz.koeln.kerner.anna.server;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import de.kerner.commons.file.LazyStringReader;
import de.kerner.commons.other.CommandStringBuilder;
import de.kerner.commons.other.jarbuilder.JarBuilder;
import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;

class LSFExecutor extends AbstractRemoteStepExecutor {
	
	private final static File JAR_FILE = new File("/home/pcb/kerner/Desktop/"
			+ "aeffchen" + ".jar");

	private static List<String> getCommandList(){
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
		JarBuilder.buildJar(clazz, JAR_FILE);
	}

	public Boolean call() throws Exception {
		final ProcessBuilder builder = new ProcessBuilder(getCommandList());
		final Process p = builder.start();
		ServerActivator.LOGGER.debug(this, "process created:"+p);
		final InputStream is = p.getInputStream();
		final InputStream os = p.getErrorStream();
		final LazyStringReader out = new LazyStringReader(is);
		final LazyStringReader err = new LazyStringReader(os);
		ServerActivator.LOGGER.debug(this, "process output:"+out.getString());
		ServerActivator.LOGGER.debug(this, "process errorout:"+err.getString());
		final int exitValue = p.waitFor();
		is.close();
		os.close();
		ServerActivator.LOGGER.debug(this, "process exited with exitcode " + exitValue);
		return true;
	}
}
