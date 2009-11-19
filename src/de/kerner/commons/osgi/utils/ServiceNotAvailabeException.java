package de.kerner.commons.osgi.utils;

public class ServiceNotAvailabeException extends Exception {

	private static final long serialVersionUID = 1138969328254030998L;

	public ServiceNotAvailabeException() {
	}

	public ServiceNotAvailabeException(String arg0) {
		super(arg0);
	}

	public ServiceNotAvailabeException(Throwable arg0) {
		super(arg0);
	}

	public ServiceNotAvailabeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
