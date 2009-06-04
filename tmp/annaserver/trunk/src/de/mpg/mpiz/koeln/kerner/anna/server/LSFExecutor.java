package de.mpg.mpiz.koeln.kerner.anna.server;

import java.util.ArrayList;
import java.util.List;

import de.kerner.commons.other.CommandStringBuilder;
import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;

@SuppressWarnings("serial")
class LSFExecutor extends AbstractRemoteStepExecutor {
	
	private final static String EXE = "bsub";
	private final static List<String> KEY_VALUE_PARAMS = new ArrayList<String>(){
		{
			add("-m");
			add("pcbcomputenodes");
		}
	};
	private final static List<String> FLAG_PARAMS = new ArrayList<String>(){
		{
			add("-K"); // blocks until job is finished.
		}
	};

	LSFExecutor(AbstractStep step) {
		super(step);
		CommandStringBuilder builder = new CommandStringBuilder(EXE);
		
	}

	public Boolean call() throws Exception {
		System.err.println("doing the LSF drop");
		return null;
	}

}
