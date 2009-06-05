package Exceptions;

@SuppressWarnings("serial")
public class EmptySubstanceNameException extends Exception {
	public EmptySubstanceNameException() {
		super("Empty substance name received");
	}

	public EmptySubstanceNameException(String s) {
		super(s);
	}
}
