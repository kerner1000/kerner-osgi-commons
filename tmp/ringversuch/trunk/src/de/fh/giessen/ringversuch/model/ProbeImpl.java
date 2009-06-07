package de.fh.giessen.ringversuch.model;

import java.util.Collection;

class ProbeImpl implements Probe {
	
	private final String ident;
	private final Collection<Analyse> substances;
	private final String laborIdent;
	
	ProbeImpl(String laborIdent, String ident, Collection<Analyse> substances){
		this.laborIdent = laborIdent;
		this.ident = ident;
		this.substances = substances;
	}

	@Override
	public String getIdentifier() {
		return ident;
	}

	@Override
	public String getLaborIdent() {
		return laborIdent;
	}

	@Override
	public Collection<Analyse> getSubstances() {
		return substances;
	}

}
