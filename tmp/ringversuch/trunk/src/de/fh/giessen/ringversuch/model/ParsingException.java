package de.fh.giessen.ringversuch.model;

public class ParsingException extends Exception {

	private static final long serialVersionUID = 5019093606901932657L;

	public ParsingException() {
	}

	public ParsingException(String message) {
		super(message);
	}

	public ParsingException(Throwable cause) {
		super(cause);
	}

	public ParsingException(String message, Throwable cause) {
		super(message, cause);
	}

}
