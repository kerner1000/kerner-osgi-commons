package de.fh.giessen.ringversuch.model;

import java.util.Collection;

class OutSubstanceEntryImpl implements OutSubstanceEntry {

	private final String ident;
	private final Collection<String> values;
	
	OutSubstanceEntryImpl(String identifier, Collection<String> values) {
		this.ident = identifier;
		this.values = values;
	}

	@Override
	public String getIdent() {
		return ident;
	}

	@Override
	public Collection<String> getValues() {
		// TODO defensive copying?
		return values;
	}

	@Override
	public String toString(){
		return ident + ":" + values;
	}
}
