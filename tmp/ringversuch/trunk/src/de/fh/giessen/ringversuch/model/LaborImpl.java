package de.fh.giessen.ringversuch.model;

import java.util.Collection;


class LaborImpl implements Labor {
	
	private final String ident;
	private final Collection<Probe> probes;
	
	LaborImpl(String ident, Collection<Probe> probes){
		this.ident = ident;
		this.probes = probes;
	}

	@Override
	public String getIdentifier() {
		return ident;
	}

	@Override
	public Collection<Probe> getProbes() {
		// TODO defensive copying ?
		return probes;
	}

}
