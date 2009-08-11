package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import de.fh.giessen.ringversuch.model.settings.ModelSettings;


class Worker implements Callable<Void> {

	private final File outDir;
	private final File[] files;
	private final ModelSettings settings;
	private final WorkMonitor monitor;
//	private final static Logger LOGGER = Logger.getLogger(Worker.class);

	Worker(File[] inputFiles, File outDir, ModelSettings settings, WorkMonitor monitor) {
		this.outDir = outDir;
		this.files = inputFiles;
		this.settings = settings;
		this.monitor = monitor;
	}

	@Override
	public Void call() throws Exception {
		final Collection<Labor> labors = new ArrayList<Labor>();
		int currentProgress = 0;
		int maxProgress = files.length * 2;
		if(Thread.currentThread().isInterrupted())
			throw new InterruptedException();
		monitor.setProgress(currentProgress, maxProgress);
		for (File f : files) {
			if(Thread.currentThread().isInterrupted())
				throw new InterruptedException();
			monitor.printMessage("reading file " + f.getName());
			labors.add(Core.readLaborFile(f, settings));
			monitor.setProgress(currentProgress++, maxProgress);
		}
		if(Thread.currentThread().isInterrupted())
			throw new InterruptedException();
		monitor.printMessage("calculating... ");
		final Collection<OutSubstance> outSubstances = Core
		.getOutSubstancesFromLabors(labors, settings);
		monitor.printMessage("calculating done");
		for(OutSubstance s : outSubstances){
			if(Thread.currentThread().isInterrupted())
				throw new InterruptedException();
			final OutSubstanceWriter writer = new OutSubstanceWriter(s, outDir);
			monitor.printMessage("writing " + writer.getOutFile());
			writer.write();
			monitor.setProgress(currentProgress++, maxProgress);
		}
		monitor.printMessage("done");
		return null;
	}
}
