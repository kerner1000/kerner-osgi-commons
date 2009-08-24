package de.fh.giessen.ringversuch.model;

import java.util.Collection;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.exception.InvalidFormatException;

class LaborImpl implements Labor {
	
	private final String ident;
	private final Collection<Probe> probes;
	private final static Logger LOGGER = Logger.getLogger(LaborImpl.class);
	
	LaborImpl(String ident, Collection<Probe> probes) throws InvalidFormatException{
		if(ident == null || ident.length() == 0 || probes == null || probes.size() == 0)
			throw new NullPointerException();
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
		LOGGER.debug("getting probe for identifier \"" + probeIdent + "\"");
		for(Probe p : probes){
			if(p.getIdentifier().equalsIgnoreCase(probeIdent)){
				LOGGER.debug("got probe: " + p);
				return p;
			}
		}
		LOGGER.error("could not get probe for identifier \"" + probeIdent + "\"." + Preferences.NEW_LINE + "registered probes: " + probes);
		return null;
	}

}
