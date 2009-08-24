package de.fh.giessen.ringversuch.model;

import hssf.utils.WrongFileTypeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.controller.ControllerOut;
import de.fh.giessen.ringversuch.exception.InvalidSettingsException;
import de.fh.giessen.ringversuch.model.settings.ModelSettings;
import de.fh.giessen.ringversuch.model.settings.ModelSettingsImpl;
import de.kerner.commons.StringUtils;

/**
 * @ThreadSave
 * @author Alexander Kerner
 * 
 */
public class ModelImpl implements Model {

	private final ExecutorService worker = Executors.newCachedThreadPool();
	private final static Logger LOGGER = Logger.getLogger(ModelImpl.class);
	private final ControllerOut controller;
	private File outDir;
	private File[] inputFiles;
	private ModelSettings settings = new ModelSettingsImpl();
	private Future<Void> currentJob;
	private Future<ModelSettings> currentDetectJob;

	public ModelImpl(ControllerOut controller) {
		this.controller = controller;
	}

	@Override
	public synchronized void checkSettings() throws Exception {
		final Boolean valid = worker.submit(new Callable<Boolean>(){
			@Override
			public Boolean call() throws Exception {
				return settings.areValid();
			}
		}).get();
		if(!valid.booleanValue())
			throw new InvalidSettingsException(Preferences.Model.SETTINGS_INVALID);
	}
	
	@Override
	public synchronized void setOutDir(File selectedDir) {
		this.outDir = selectedDir;
		final String m = StringUtils.getString("files will be written to ", outDir);
		LOGGER.info(m);
		controller.printMessage(m, false);
	}

	@Override
	public synchronized void setSelectedFiles(File[] inputFiles) throws WrongFileTypeException {
		checkFiles(inputFiles);
		this.inputFiles = inputFiles;
//		LOGGER.info("input files: " + Arrays.asList(inputFiles));
		final String m = StringUtils.getString("selected ", Arrays.asList(inputFiles).size(), " file(s)");
		controller.printMessage(m, false);
	}

	private void checkFiles(File[] files) throws WrongFileTypeException {
		for(File f : files){
			try {
				new POIFSFileSystem(
						new FileInputStream(f));
			}catch (IOException e) {
				LOGGER.error(e.getLocalizedMessage(), e);
				throw new WrongFileTypeException(f.getName() + " seems not to be a valid xls file");
			}catch(OfficeXmlFileException e){
				LOGGER.error(e.getLocalizedMessage(), e);
				throw new WrongFileTypeException(f.getName() + " seems not to be a valid xls file");
			}
		}
	}

	@Override
	public synchronized void detectProbeCell() throws Exception {
		currentDetectJob = worker.submit(new ProbeCellDetector(inputFiles,
				getSettings(), new WorkMonitor(controller)));
		this.settings = currentDetectJob.get();
		currentDetectJob = null;
	}

	@Override
	public synchronized void detectLaborCell() throws Exception {
		currentDetectJob = worker.submit(new LaborCellDetector(inputFiles,
				getSettings(), new WorkMonitor(controller)));
		this.settings = currentDetectJob.get();
		currentDetectJob = null;
	}

	@Override
	public synchronized void detectValuesBeginCell() throws Exception {
		currentDetectJob = worker.submit(new ValuesBeginCellDetector(
				inputFiles, getSettings(), new WorkMonitor(controller)));
		this.settings = currentDetectJob.get();
		currentDetectJob = null;
	}

	@Override
	public synchronized void detectValuesEndCell() throws Exception {
		currentDetectJob = worker.submit(new ValuesEndCellDetector(
				inputFiles, getSettings(), new WorkMonitor(controller)));
		this.settings = currentDetectJob.get();
		currentDetectJob = null;
	}

	@Override
	public synchronized void detectColumnOfSubstances() throws Exception {
		currentDetectJob = worker.submit(new ColumnOfSubstancesDetector(
				inputFiles, getSettings(), new WorkMonitor(controller)));
		this.settings = currentDetectJob.get();
		currentDetectJob = null;
	}

	@Override
	public synchronized void start() throws CancellationException,
			InterruptedException, ExecutionException {
		LOGGER.info("starting...");
		currentJob = worker.submit(new Worker(inputFiles, outDir,
				settings, new WorkMonitor(controller)));
		currentJob.get();
		LOGGER.info("done!");
		currentJob = null;
	}

	@Override
	public synchronized ModelSettings getSettings() {
		return settings;
	}

	@Override
	public synchronized void setSettings(ModelSettings settings) {
		final String m = StringUtils.getString("new settings: ", settings);
		LOGGER.debug(m);
		this.settings = settings;
	}

	/**
	 * Must not be synchronized !!
	 */
	@Override
	public void cancel() {
		if (currentJob != null)
			currentJob.cancel(true);
		if (currentDetectJob != null)
			currentDetectJob.cancel(true);
	}
}
