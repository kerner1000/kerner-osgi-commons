package de.mpg.mpiz.koeln.kerner.anna.starter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import de.jcommandlineparser.CmdLineParser;
import de.jcommandlineparser.CmdLineParserImpl;
import de.jcommandlineparser.FileOption;
import de.jcommandlineparser.OptionBuilder;
import de.kerner.commons.file.FileUtils;

public class Starter {

	private final static Logger LOGGER = Logger.getLogger(Starter.class);

	private final static String LOG_FILE_PATH = FileUtils.WORKING_DIR
			+ "/configuration/log.properties";
	private final static String PLUGINS_PATH = FileUtils.WORKING_DIR
			+ "/plugins";

	private void start(final String additionalBundlePath) throws Exception {
		PropertyConfigurator.configure(LOG_FILE_PATH);
		final BundleContext context = EquinoxRunner.start(null);
		File pluginDir = null;
		// install default bundles
		pluginDir = new File(PLUGINS_PATH);
		final List<String> plugins = getPLuginFiles(pluginDir);
		// install additional bundles
		if (additionalBundlePath != null && additionalBundlePath.length() != 0) {
			pluginDir = new File(additionalBundlePath);
			plugins.addAll(getPLuginFiles(pluginDir));
		}
		List<Bundle> installedPLugins = installPlugins(context, plugins);
		startPlugins(context, installedPLugins);
	}

	private void startPlugins(BundleContext context,
			List<Bundle> installedPLugins) throws BundleException {
		if (installedPLugins.size() == 0)
			return;
		if (context == null)
			throw new NullPointerException("BundleContext may not be null");
		for (Bundle b : installedPLugins) {
			b.start();
		}
	}

	private List<Bundle> installPlugins(BundleContext context,
			List<String> plugins) throws BundleException {
		if (plugins != null && plugins.size() == 0)
			return Collections.emptyList();
		if (context == null)
			throw new NullPointerException("BundleContext may not be null");
		List<Bundle> result = new ArrayList<Bundle>();
		for (String s : plugins) {
			LOGGER.debug("installing " + s);
			LOGGER.debug("context="+context);
			final Bundle b = context.installBundle(s);
			result.add(b);
		}
		return result;
	}

	private List<String> getPLuginFiles(File dir) throws FileNotFoundException,
			NoPluginsFoundException {
		if (dir == null || dir.isFile())
			throw new FileNotFoundException("Could not find directory " + dir);
		final List<String> result = new ArrayList<String>();
		final File[] content = dir.listFiles();
		if (content == null || content.length == 0) {
			throw new NoPluginsFoundException("Could not find any plugins in "
					+ dir);
		} else {
			for (File f : content) {
				if (f.isFile()) {
					if (f.canRead()) {
						final String s = f.toURI().toString();
						if (s.endsWith(".jar")) {
							LOGGER.debug("Adding " + f
									+ " to known plugins list.");
							result.add(s);
						}
					} else {
						LOGGER.error("Cannot read " + f + ". Skipping.");
					}
				} else {
					// ignore dirs
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		try {
			if(args == null || args.length == 0){
				new Starter().start(null);
			}else {
			final CmdLineParser parser = parseAndGet(args);
			final File f1 = parser.getValues('d', File.class).get(0);
			new Starter().start(f1.getAbsolutePath());
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	private static CmdLineParser parseAndGet(String[] args) throws Exception {
		final CmdLineParser parser = new CmdLineParserImpl();
		OptionBuilder.buildNewOption("additional-dir", 'd');
		final FileOption additionalBundlePath = OptionBuilder
				.getNewFileOption();
		parser.registerOption(additionalBundlePath);
		parser.parse(args);
		return parser;
	}

}
