package de.fh.giessen.ringversuch.model;

import java.util.Collection;

/**
 * @ThreadSave state final
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 *
 */
class OutSubstanceEntryImpl implements OutSubstanceEntry {

	private final Collection<String> values;
	private final Labor labor;
	
	OutSubstanceEntryImpl(Labor labor, Collection<String> values) {
		if(labor == null || values == null || values.size() == 0)
			throw new NullPointerException();
		this.values = values;
		this.labor = labor;
	}

	@Override
	public Collection<String> getValues() {
		// TODO defensive copying?
		return values;
	}

	@Override
	public String toString(){
		return labor.getIdentifier() + ":" + values;
	}

	@Override
	public Labor getLabor() {
		return labor;
	}
}
