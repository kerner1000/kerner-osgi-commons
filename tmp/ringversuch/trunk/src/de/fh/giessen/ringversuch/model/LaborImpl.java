package de.fh.giessen.ringversuch.model;

import java.util.Collection;


class LaborImpl implements Labor {
	
	private final String ident;
	private final Collection<Probe> probes;
	
	LaborImpl(String ident, Collection<Probe> probes) throws InvalidFormatException{
		// TODO check if probeIdents are unique
		verifyLaborIdents(ident, probes);
		this.ident = ident;
		this.probes = probes;
	}

	private void verifyLaborIdents(String ident2, Collection<Probe> probes2) throws InvalidFormatException {
		for(Probe p : probes2){
			if(!p.getLaborIdent().equalsIgnoreCase(ident2))
				throw new InvalidFormatException("inconsistent labor identification for labor \"" + ident2 + "\" and probe \"" + p.getIdentifier() + "\"");
		}
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
	
	@Override
	public String toString(){
		return ident + ":" + probes;
	}

	@Override
	public Probe getProbe(String probeIdent) {
		// TODO defensive copying ?
		for(Probe p : probes){
			if(p.getIdentifier().equalsIgnoreCase(probeIdent)){
				return p;
			}
		}
		return null;
	}

}
