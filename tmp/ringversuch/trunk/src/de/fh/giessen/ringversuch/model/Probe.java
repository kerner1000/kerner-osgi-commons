package de.fh.giessen.ringversuch.model;

import java.util.Collection;

interface Probe {
	
	String getIdentifier();
	String getLaborIdent();
	Collection<Analyse> getAnalyses();
	Collection<String> getCommonSubstanceKeys();

}
