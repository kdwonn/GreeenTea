package testexample;

/**
 *  The interest of simple interest account is 3%.
 *  For example, assume someone puts 100 dollars in his simple interest account.
 *  Then, after 10 days, the balance of the account will be 100*(1+0.03*10).
 */
public class SimpleInterest extends Account {
	public final static double interest = 0.03;
	final double initialFund;
	/**
	 *  Create Simple Interest Account 
	 *  
	 *  @param num_number the new account number
	 *  @param name the name of account owner
     *  @param initial initial balance when an account is created	 
	 */
	public SimpleInterest(int new_number, String name, double initial) {
		super(new_number, name, initial);
		initialFund = initial;
	}
	
	/**
	 *  Update the balance. Interest of the account is calculated at simple interest.
	 *  
	 *  @param elapsedDate elapsed date
	 */
	@Override
	public
	void updateBalance(int elapsedDate) {
		super.balance = initialFund * (1 + interest * elapsedDate);
	}
}

