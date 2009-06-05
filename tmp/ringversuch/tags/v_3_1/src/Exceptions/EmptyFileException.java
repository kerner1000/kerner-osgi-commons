package Exceptions;

@SuppressWarnings("serial")
public class EmptyFileException extends Exception {
	public EmptyFileException() {
		super("File seems to be empty! Skipping file");
	}

	public EmptyFileException(String s) {
		super(s);
	}
}
