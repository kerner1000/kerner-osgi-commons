package de.fh.giessen.ringversuch.model;

import java.util.Collection;

class OutSubstanceImpl implements OutSubstance {
	
	private final String probeIdent, substanceIdent;
	private final Collection<OutSubstanceEntry> entries;
	
	OutSubstanceImpl(String ident, String probeIdent, Collection<OutSubstanceEntry> entries) {
		this.entries = entries;
		this.probeIdent = probeIdent;
		this.substanceIdent = ident;
	}
	
	@Override
	public String toString(){
		return probeIdent + " " + substanceIdent + " " + entries;
	}

	@Override
	public Collection<OutSubstanceEntry> getEntries() {
		// TODO defensive copying?
		return entries;
	}

	@Override
	public String getIdent() {
		return substanceIdent;
	}

	@Override
	public String getProbeIdent() {
		return probeIdent;
	}

}