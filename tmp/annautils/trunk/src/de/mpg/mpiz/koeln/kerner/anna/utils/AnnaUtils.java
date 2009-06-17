package de.mpg.mpiz.koeln.kerner.anna.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipException;

import org.apache.log4j.Logger;

import de.kerner.commons.file.FileUtils;

public class AnnaUtils {

	private final static String ACTIVATOR_KEY = "Bundle-Activator";
	private final static String IMPORT_KEY = "Import-Package";

	// private final static Logger LOGGER = Logger.getLogger(AnnaUtils.class);

	private AnnaUtils() {

	}

	public static void ConvertBundleToRunnableJar(final File bundleFile,
			final File jarFile, final List<File> dependencies)
			throws IOException {
		final JarFile inFile = new JarFile(bundleFile);
		final Manifest mani = inFile.getManifest();
		final Attributes attributes = mani.getMainAttributes();
		final String mainClass = attributes.getValue(ACTIVATOR_KEY);
		System.err.println("MAIN-CLASS=" + mainClass);
		final File tmpDir = new File("/home/pcb/kerner/Desktop/tmpDir");
		extractToTmpDir(bundleFile, tmpDir);
		if(dependencies.size() != 0){
			for(File f : dependencies){
				extractToTmpDir(f, tmpDir);
			}
		}
		createJarFile(jarFile, tmpDir, mainClass);
	}

	private static void createJarFile(final File jarFile, final File dir, final String mainClassName) throws IOException {
		de.kerner.commons.zip.ZipUtils.createRunnableJarFromDirectory(dir, jarFile, mainClassName);
	}

	private static void extractToTmpDir(final File bundleFile, final File dir)
			throws ZipException, IOException {
		de.kerner.commons.zip.ZipUtils.extractToDir(bundleFile, dir);
	}

	public static <V> V fileToObject(Class<V> class1, File file)
			throws IOException, ClassNotFoundException {
		return FileUtils.fileToObject(class1, file);
	}

}
