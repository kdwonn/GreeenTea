package testexample;

class NegativeException extends Exception{
	public NegativeException() {
		super("Insufficient account balance.");
	}
}