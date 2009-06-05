package Exceptions;

@SuppressWarnings("serial")
public class ProbeNoNotFoundException extends Exception {

	public ProbeNoNotFoundException() {
		super("No valid probe no. found");
	}

	public ProbeNoNotFoundException(String s) {
		super(s);
	}
}
