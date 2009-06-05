package Exceptions;

@SuppressWarnings("serial")
public class LaboratoryNotFoundException extends Exception {
	public LaboratoryNotFoundException() {
		super("No laboratory identification found");
	}

	public LaboratoryNotFoundException(String s) {
		super(s);
	}
}
