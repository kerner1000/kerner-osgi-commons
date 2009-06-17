package de.mpg.mpiz.koeln.kerner.anna.step.conrad.train;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kerner.commons.CommandStringBuilder;

class RunStateLSF extends AbstractRunState {
	
	private final static String BSUB_EXE = "bsub";
	
	@Override
	List<String> getCommandList() {
		final CommandStringBuilder builder = new CommandStringBuilder(BSUB_EXE);
		builder.addAllFlagCommands(getBsubFlagCommandStrings());
		builder.addAllValueCommands(getBsubValueCommandStrings());
		builder.addFlagCommand(CONRAD_EXE);
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(WORKING_DIR.getAbsolutePath());
		final File trainingFile = new File(WORKING_DIR, TRAINING_FILE_NAME);
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		return builder.getCommandList();
	}

	private Map<String, String> getBsubValueCommandStrings() {
		final Map<String, String> map = new HashMap<String,String>();
		map.put("-m", "pcbcn64");
		return map;
	}

	private List<String> getBsubFlagCommandStrings() {
		final List<String> list = new ArrayList<String>();
		list.add("-K");
		return list;
	}

}
