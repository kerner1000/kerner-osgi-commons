package de.fh.giessen.ringversuch.model;

import java.util.Collection;

interface Labor {
	
	String getIdentifier();
	Collection<Probe> getProbes();

}
