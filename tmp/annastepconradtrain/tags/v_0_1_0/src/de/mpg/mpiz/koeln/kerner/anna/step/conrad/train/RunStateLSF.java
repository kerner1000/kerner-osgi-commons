package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

class RunStateLSF extends AbstractRunStateTraining {
	
	RunStateLSF(File conradWorkingDir, File workingDir, File trainingFile,
			File fastaFile, File gtfFile, LogDispatcher logger) {
		super(conradWorkingDir, workingDir, trainingFile, fastaFile, gtfFile, logger);
		LSFout = new File(workingDir, "lsf-%J-%I.out");
		LSFerr = new File(workingDir, "lsf-%J-%I.err");
	}

	private final static String BSUB_EXE = "bsub";
	private final File LSFout, LSFerr;
	
	
	
	@Override
	protected
	List<String> getCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(BSUB_EXE);
		builder.addAllFlagCommands(getBsubFlagCommandStrings());
		builder.addAllValueCommands(getBsubValueCommandStrings());
		builder.addFlagCommand(ConradConstants.CONRAD_EXE);
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(workingDir.getAbsolutePath());
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		return builder.getCommandList();
	}

	private Map<String, String> getBsubValueCommandStrings() {
		final Map<String, String> map = new HashMap<String,String>();
		map.put("-m", "pcbcn64");
		map.put("-eo", LSFerr.getAbsolutePath());
		map.put("-oo", LSFout.getAbsolutePath());
		return map;
	}

	private List<String> getBsubFlagCommandStrings() {
		final List<String> list = new ArrayList<String>();
		list.add("-K");
		return list;
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
