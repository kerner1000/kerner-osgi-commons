package de.fh.giessen.ringversuch.model;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

class ProbeImpl implements Probe {
	
	private final static Logger LOGGER = Logger.getLogger(ProbeImpl.class);
	private final String ident;
	private final Collection<Analyse> analyses;
	private final String laborIdent;
	
	ProbeImpl(String laborIdent, String ident, Collection<Analyse> analyses){
		this.laborIdent = laborIdent;
		this.ident = ident;
		this.analyses = analyses;
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
	public Collection<Analyse> getAnalyses() {
		return analyses;
	}
	
	@Override
	public String toString(){
		return "probeIdent:" + ident + ",laborIdent:"+laborIdent+",substances:"+analyses;
	}

	@Override
	public Collection<String> getCommonSubstanceKeys() {
		Collection<String> result = new ArrayList<String>();
		for(Substance s : analyses.iterator().next().getSubstances()){
			final String key = s.getIdentifier();
			if(keyContainedInEveryAnalyse(key))
				result.add(key);
			else {
				LOGGER.error("Warning: Substance " + key + " is not contained in every analyse of probe " + this.getIdentifier());
			}
		}
		return result;
	}

	private boolean keyContainedInEveryAnalyse(String key) {
		Collection<String> keys = null;
		for(Analyse a : analyses){
			Collection<String> tmpKeys = a.getSubstancesKeys();
			if(keys == null){
				keys = new ArrayList<String>(tmpKeys);
			} else {
				return Core.collectionsAreEqual(keys, tmpKeys);
			}
		}
		return false;
	}	
}
