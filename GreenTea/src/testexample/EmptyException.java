package testexample;

class EmptyException extends Exception{
	public EmptyException() {
		super("No such account found.");
	}
}