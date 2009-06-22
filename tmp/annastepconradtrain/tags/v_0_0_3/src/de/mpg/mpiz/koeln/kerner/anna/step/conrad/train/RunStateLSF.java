package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.kerner.anna.step.conrad.common.ConradConstants;

class RunStateLSF extends AbstractRunStateTraining {
	
	private final static String BSUB_EXE = "bsub";
	private final File LSFout, LSFerr;
	
	RunStateLSF(File workingDir, File conradWorkingDir, String trainingFileName) {
		super(workingDir, conradWorkingDir, trainingFileName);
		LSFout = new File(workingDir, "lsf-%J-%I.out");
		LSFerr = new File(workingDir, "lsf-%J-%I.err");
	}
	
	@Override
	protected
	List<String> getCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(BSUB_EXE);
		builder.addAllFlagCommands(getBsubFlagCommandStrings());
		builder.addAllValueCommands(getBsubValueCommandStrings());
		builder.addFlagCommand(ConradConstants.CONRAD_EXE);
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(stepWorkingDir.getAbsolutePath());
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
