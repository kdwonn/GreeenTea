package testexample;

public class NegativeException extends Exception{
	public NegativeException() {
		super("Insufficient account balance.");
	}
}