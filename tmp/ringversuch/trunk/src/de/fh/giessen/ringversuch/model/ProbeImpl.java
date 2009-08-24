package de.fh.giessen.ringversuch.model;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * @threadSave state final
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 *
 */
class ProbeImpl implements Probe {
	
	private final static Logger LOGGER = Logger.getLogger(ProbeImpl.class);
	private final String ident;
	private final Collection<Analyse> analyses;
	private final String laborIdent;
	
	ProbeImpl(String laborIdent, String ident, Collection<Analyse> analyses){
		if(laborIdent == null || laborIdent.length() == 0 || ident == null || ident.length() == 0 || analyses == null || analyses.size() == 0)
			throw new NullPointerException();
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
		LOGGER.debug("getting substances identificators");
		Collection<String> result = new ArrayList<String>();
		for(Substance s : analyses.iterator().next().getSubstances()){
			final String key = s.getIdentifier();
			if(keyContainedInEveryAnalyse(key))
				result.add(key);
			else {
				LOGGER.error("Warning: Substance " + key + " is not contained in every analyse of probe " + this.getIdentifier());
			}
		}
		LOGGER.debug("got substances identificators: " + result);
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
