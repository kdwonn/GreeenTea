package testexample;

class NotFoundException extends Exception{
	public NotFoundException() {
		super("No such account.");
	}
	
}