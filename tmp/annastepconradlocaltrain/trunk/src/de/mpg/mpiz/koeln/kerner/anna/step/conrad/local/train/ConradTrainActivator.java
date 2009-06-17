package de.mpg.mpiz.koeln.kerner.anna.step.conrad.local.train;

import java.io.File;
import java.util.ArrayList;

import org.bioutils.fasta.FASTASequence;
import org.bioutils.gtf.GTFElement;

import de.mpg.mpiz.koeln.kerner.anna.core.AbstractStep;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;

public class ConradTrainActivator extends AbstractStep {

	public ConradTrainActivator() {

	}

	@Override
	public boolean checkRequirements(DataBean data) {
		try {
			return (data.getVerifiedGenesFasta() != null
					&& data.getVerifiedGenesFasta().size() != 0
					&& data.getVerifiedGenesGtf() != null && data
					.getVerifiedGenesGtf().size() != 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(this + " could not access data ");
			return false;
		}
	}

	@Override
	public DataBean run(DataBean data) throws Exception {
		final ArrayList<? extends FASTASequence> fastas = data
				.getVerifiedGenesFasta();
		final ArrayList<? extends GTFElement> elements = data
				.getVerifiedGenesGtf();
		System.out.println(this + " going to run, creating wrapper");
		final ConradTrainWrapper wrapper = new ConradTrainWrapper(fastas, elements);
		final boolean b = wrapper.run();
		if(b == false){
			System.out.println(this + " training not sucessfull, cannot update data");
		} else {
			final File trainingFile = wrapper.getResult();
			System.out.println(this + " training sucessfull, created training file " + trainingFile);
			data.setConradTrainingFile(trainingFile);
		}
		return data;
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
