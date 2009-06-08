package de.fh.giessen.ringversuch.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class AnalyseImpl implements Analyse {

	private final String identifier;
	private final Collection<Substance> substances;

	AnalyseImpl(String identifier, Collection<Substance> substances){
		this.identifier = identifier;
		this.substances = substances;
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public Collection<Substance> getSubstances() {
		return substances;
	}
	
	@Override
	public String toString(){
		return identifier + ":" + substances;
	}

	@Override
	public Collection<String> getSubstancesKeys() {
		Collection<String> result = new ArrayList<String>();
		for(Substance s : substances){
			result.add(s.getIdentifier());
		}
		return result;
	}

	@Override
	public String getValueForSubstance(String substanceIdent) {
		for(Substance s : substances){
			if(s.getIdentifier().equalsIgnoreCase(substanceIdent)){
				return s.getValue();
			}
		}
		return null;
	}
}
