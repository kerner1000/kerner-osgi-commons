package de.fh.giessen.ringversuch.model;

import java.util.Collection;

interface Analyse {

	String getIdentifier();
	Collection<Substance> getSubstances();
	Collection<String> getSubstancesKeys();
	String getValueForSubstance(String substanceIdent);
}
