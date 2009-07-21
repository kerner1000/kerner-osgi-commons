package de.fh.giessen.ringversuch.model;

import java.util.Collection;

interface OutSubstance {
	
	String getProbeIdent();
	String getSubstanceIdent();
	Collection<OutSubstanceEntry> getEntries();

}
