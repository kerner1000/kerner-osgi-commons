package de.fh.giessen.ringversuch.model;

import java.util.Collection;

/**
 * not public, so no doc
 * 
 * @author Alexander Kerner
 * 
 */
interface Analyse {

	String getIdentifier();

	Collection<Substance> getSubstances();

	Collection<String> getSubstancesKeys();

	String getValueForSubstance(String substanceIdent);
}
