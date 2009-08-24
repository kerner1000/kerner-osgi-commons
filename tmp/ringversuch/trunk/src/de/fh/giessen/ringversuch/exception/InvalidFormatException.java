package de.fh.giessen.ringversuch.exception;

public class InvalidFormatException extends Exception {

	private static final long serialVersionUID = 2141887722610584432L;

	public InvalidFormatException() {
	}

	public InvalidFormatException(String message) {
		super(message);
	}

	public InvalidFormatException(Throwable cause) {
		super(cause);
	}

	public InvalidFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
