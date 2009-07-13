package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;


class Worker implements Callable<Boolean> {

	private final File outDir;
	private final File[] files;
	private final SettingsModel settings;

	Worker(File[] inputFiles, File outDir, SettingsModel settings) {
		this.outDir = outDir;
		this.files = inputFiles;
		this.settings = settings;
	}

	@Override
	public Boolean call() throws Exception {
		final boolean success = true;
		final Collection<Labor> labors = new ArrayList<Labor>();
		for (File f : files) {
			labors.add(Core.readLaborFile(f, settings));
		}
		final Collection<OutSubstance> outSubstances = Core
		.getOutSubstancesFromLabors(labors, settings);
		for(OutSubstance s : outSubstances){
			final String fileName = Core.getFileNameForOutSubstance(s);
			Core.writeOutSubstance(s, new File(outDir, fileName + ".xls"));
		}
		return success;
	}

}
