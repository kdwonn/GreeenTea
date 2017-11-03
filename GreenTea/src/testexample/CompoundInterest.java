package testexample;

/**
 *  The interest of compound interest account is 1%.
 *  For example, assume someone puts 100 dollars in his compound interest account.
 *  Then, after 10 days, the balance of the account will be 100*(1.01)^10.
 */
class CompoundInterest extends Account {
	final static double interest = 0.01;
	
	/**
	 *  Create a compound interest account. 
	 *  
	 *  @param newNumber the new account number
	 *  @param name the name of account owner
     *  @param initial initial balance when an account is created	  
	 */
	CompoundInterest(int newNumber, String name, double initial) {
		super(newNumber, name, initial);
	}
	
	/**
	 *  Update the balance. Interest on the account was compounded daily.
	 *  
	 *  @param elapsedDate elapsed date
	 */
	void updateBalance(int elapsedDate) {
		super.balance *= Math.pow((1 + interest),elapsedDate);
	}
}