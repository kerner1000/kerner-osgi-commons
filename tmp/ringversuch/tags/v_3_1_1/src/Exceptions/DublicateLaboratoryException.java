package Exceptions;

@SuppressWarnings("serial")
public class DublicateLaboratoryException extends Exception {
	public DublicateLaboratoryException() {
		super("Dublicate laboratory identification");
	}

	public DublicateLaboratoryException(String s) {
		super(s);
	}
}
