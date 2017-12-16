package testexample;

public class EmptyException extends Exception{
	public EmptyException() {
		super("No such account found.");
	}
}