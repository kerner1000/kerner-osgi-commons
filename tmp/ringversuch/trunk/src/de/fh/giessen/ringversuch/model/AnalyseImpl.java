package de.fh.giessen.ringversuch.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * not public, so no doc
 * 
 * @ThreadSave state final
 * @lastVisit 2009-08-11
 * @author Alexander Kerner
 * 
 */
class AnalyseImpl implements Analyse {

	private final String identifier;
	private final Collection<Substance> substances;

	AnalyseImpl(String identifier, Collection<Substance> substances) {
		if(substances == null || substances.size() == 0)
			throw new NullPointerException("substances="+substances);
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
	public String toString() {
		return identifier + ":" + substances;
	}

	@Override
	public Collection<String> getSubstancesKeys() {
		final Collection<String> result = new ArrayList<String>();
		for (Substance s : substances) {
			result.add(s.getIdentifier());
		}
		return result;
	}

	@Override
	public String getValueForSubstance(String substanceIdent) {
		for (Substance s : substances) {
			if (s.getIdentifier().equalsIgnoreCase(substanceIdent)) {
				return s.getValue();
			}
		}
		return null;
	}
}
