package de.fh.giessen.ringversuch.model;

public class FailedToDetectException extends Exception {

	private static final long serialVersionUID = -3412234730178584380L;

	public FailedToDetectException() {
	}

	public FailedToDetectException(String arg0) {
		super(arg0);
	}

	public FailedToDetectException(Throwable arg0) {
		super(arg0);
	}

	public FailedToDetectException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
