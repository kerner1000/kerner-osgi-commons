package de.fh.giessen.ringversuch.model;

import java.util.Collection;

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

}
