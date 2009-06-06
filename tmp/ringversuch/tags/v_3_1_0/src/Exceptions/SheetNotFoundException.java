package Exceptions;

@SuppressWarnings("serial")
public class SheetNotFoundException extends Exception {

	public SheetNotFoundException() {
		super();
	}

	public SheetNotFoundException(String s) {
		super(s);
	}
}
