package de.fh.giessen.ringversuch.model;

/**
 * @lastVisit 2009-08-24
 * @threadSave state final
 * @author Alexander Kerner
 *
 */
class SubstanceImpl implements Substance {
	
	private final String identifier;
	private final String value;

	SubstanceImpl(String identifier, String value) {
		if(identifier == null || identifier.length() == 0 || value == null || value.length() == 0)
			throw new NullPointerException();
		this.identifier = identifier;
		this.value = value;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return identifier + ":" + value;
	}

}
