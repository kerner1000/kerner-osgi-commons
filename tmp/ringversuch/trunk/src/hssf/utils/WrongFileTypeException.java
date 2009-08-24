package hssf.utils;

public class WrongFileTypeException extends Exception {

	private static final long serialVersionUID = 4564994329368888996L;

	public WrongFileTypeException() {
		
	}

	public WrongFileTypeException(String message) {
		super(message);
		
	}

	public WrongFileTypeException(Throwable cause) {
		super(cause);
		
	}

	public WrongFileTypeException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
