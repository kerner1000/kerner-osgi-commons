package de.fh.giessen.ringversuch.model;

import java.util.Collection;

interface Probe {
	
	String getIdentifier();
	String getLaborIdent();
	Collection<Analyse> getSubstances();

}
