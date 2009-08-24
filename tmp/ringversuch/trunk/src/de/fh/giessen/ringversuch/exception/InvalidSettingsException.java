package de.fh.giessen.ringversuch.exception;

public class InvalidSettingsException extends Exception {

	private static final long serialVersionUID = -5511448999987378059L;

	public InvalidSettingsException() {
	}

	public InvalidSettingsException(String arg0) {
		super(arg0);
	}

	public InvalidSettingsException(Throwable arg0) {
		super(arg0);
	}

	public InvalidSettingsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
