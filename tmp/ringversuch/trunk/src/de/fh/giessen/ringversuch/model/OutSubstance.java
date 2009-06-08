package de.fh.giessen.ringversuch.model;

import java.util.Collection;

interface OutSubstance {
	
	String getProbeIdent();
	String getIdent();
	Collection<OutSubstanceEntry> getEntries();

}
