package de.fh.giessen.ringversuch.model;

import java.util.Collection;

class OutSubstanceEntryImpl implements OutSubstanceEntry {

	private final Collection<String> values;
	private final Labor labor;
	
	OutSubstanceEntryImpl(Labor labor, Collection<String> values) {
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
