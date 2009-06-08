package de.fh.giessen.ringversuch.model;

class SubstanceImpl implements Substance {
	
	private final String identifier;
	private final String value;

	SubstanceImpl(String identifier, String value) {
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
