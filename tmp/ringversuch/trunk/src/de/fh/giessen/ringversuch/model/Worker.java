package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

class Worker implements Callable<Boolean> {

	final File outDir;
	final File[] files;
	private final SettingsManager settings = SettingsManagerImpl.INSTANCE;

	Worker(File[] inputFiles, File outDir) {
		this.outDir = outDir;
		this.files = inputFiles;
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
