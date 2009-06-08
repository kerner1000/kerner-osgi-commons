package de.fh.giessen.ringversuch.model;

import java.util.Collection;
import java.util.Iterator;

interface Labor {
	
	String getIdentifier();
	Collection<Probe> getProbes();
	Probe getProbe(String probeIdent);
}
