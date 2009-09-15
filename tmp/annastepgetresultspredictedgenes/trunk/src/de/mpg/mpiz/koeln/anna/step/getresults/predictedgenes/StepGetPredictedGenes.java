package de.mpg.mpiz.koeln.anna.step.getresults.predictedgenes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.bioutils.gtf.GTFElement;
import de.bioutils.gtf.GTFFile;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public class StepGetPredictedGenes extends AbstractStep {

	private final static String OUT_DIR_KEY = "anna.step.getResults.outDir";
	private final static String OUT_FILE_NAME_KEY = "anna.step.getResults.predictedGenes.fileName";

	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			final ArrayList<? extends GTFElement> elements = data
					.getPredictedGenesGtf();
			// TODO predicted genes may be size==0
			return (elements != null && elements.size() != 0);
		} catch (Throwable t) {
			StepUtils.handleException(this, t);
			// cannot be reached
			return false;
		}
	}

	public boolean canBeSkipped(DataProxy data)
			throws StepExecutionException {
		return false;
	}

	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		boolean success = false;
		try {
			final File outDir = new File(super.getStepProperties().getProperty(
					OUT_DIR_KEY));
			success = checkOutDir(outDir);
			if (success) {
				writeAllToOne(outDir, data);
				writeAllToSeparateFile(outDir, data);
			}
		} catch (Throwable t) {
			StepUtils.handleException(this, t);
			// cannot be reached
			return false;
		}
		return success;
	}

	private void writeAllToSeparateFile(File outDir, DataProxy data) throws DataBeanAccessException, IOException {
		ArrayList<? extends GTFElement> ele = data.getPredictedGenesGtf();
		final GTFFile file = new GTFFile(ele);
		Map<String, List<GTFElement>> set = splitToSeqNames(file);
		for(Entry<String, List<GTFElement>> e : set.entrySet()){
			String fileName = super.getStepProperties()
			.getProperty(OUT_FILE_NAME_KEY);
			final int dot = fileName.lastIndexOf(".");
			final String s1 = fileName.substring(0, dot);
			final String s2 = fileName.substring(dot);
			fileName = s1 + "_" + e.getKey() + s2;
			final File outFile = new File(outDir, fileName);
			logger.info(this, ": writing predicted genes to "
					+ outFile);
			final GTFFile file2 = new GTFFile(e.getValue());
			file2.write(outFile);
		}
		
	}

	private static Map<String, List<GTFElement>> splitToSeqNames(GTFFile file) {
		final Map<String, List<GTFElement>> map = new HashMap<String, List<GTFElement>>();
		for(GTFElement e : file.getElements()){
			final String id = e.getSeqName();
			if(map.containsKey(id)){
				map.get(id).add(e);
			} else {
				final List<GTFElement> l = new ArrayList<GTFElement>();
				l.add(e);
				map.put(id, l);
			}
		}
		return map;
	}

	private void writeAllToOne(File outDir, DataProxy data) throws DataBeanAccessException, IOException {
		final File outFile = new File(outDir, super.getStepProperties()
				.getProperty(OUT_FILE_NAME_KEY));
		logger.info(this, ": writing predicted genes to "
				+ outFile);
		final GTFFile file = new GTFFile(data
				.getPredictedGenesGtf());
		file.write(outFile);	
	}

	private boolean checkOutDir(File outFile) {
		if (!outFile.exists()) {
			logger.info(this, outFile
					+ " does not exist, creating");
			final boolean b = outFile.mkdirs();
			return b;
		}
		return outFile.canWrite();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
