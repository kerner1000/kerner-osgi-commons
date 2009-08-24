package de.fh.giessen.ringversuch.model;

import java.util.Collection;

/**
 * @lastVisit 2009-08-24
 * @author Alexander Kerner
 * 
 */
interface Analyse {

	String getIdentifier();

	// TODO: substances unique? maybe choose "Set" for collection
	Collection<Substance> getSubstances();

	// TODO: substances unique? maybe choose "Set" for collection
	Collection<String> getSubstancesKeys();

	String getValueForSubstance(String substanceIdent);
}
