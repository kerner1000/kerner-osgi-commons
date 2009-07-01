package de.mpg.mpiz.koeln.kerner.anna.starter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.osgi.framework.BundleContext;

class EquinoxRunner {

	private EquinoxRunner() {

	}

	public static BundleContext start(String path) throws Exception {
		if (path != null)
			setProperties(path);
		return EclipseStarter.startup(new String[] { "-console", "-clean" },
				null);
	}

	private static void setProperties(String path) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(path)));
		EclipseStarter.setInitialProperties(properties);
	}
}
