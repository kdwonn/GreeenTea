package testexample;

/**
 *  This class contains the information needed to represent an account.
 *  Account should have information such as owner's name, account number and balance. 
 */
public abstract class Account {
	int accountNumber;
	protected double balance;
	String owner;

	/**
	 *  This constructor is used when account is created in "Bank" object.
	 * 
	 *  @param newNumber the new account number 
     *  @param name the name of account owner
     *  @param initialFund initial balance when an account is created
	 */	
	Account(int newNumber, String name, double initialFund) {
		accountNumber = newNumber;
		owner = name;
		balance = initialFund;
	}
	
	/**
	 *  This function takes elapsed date and updates account balance accordingly.
	 *  
	 *  @param elapsedDate elapsed date
	 */
	public abstract void updateBalance(int elapsedDate);
	
	/**
	 *  Returns the account balance.
	 *  
	 *  @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	
	/**
	 *  Add as much money as a given amount factor to the given account.
	 *  
	 *  @param amount deposit amount
	 */
	public void deposit(double amount) {
		balance += amount;
		//음수인 경우 고려X
	}
	
	/**
	 *  Withdraw as much money as a given amount factor to the given account.
	 *  You have to raise exception in case when the balance of the account
	 *  is smaller than the amount of money that you want to withdraw.
	 *  Please use NegativeException we gave you.
	 *  
	 *  @param amount withdraw amount 
	 */
	public void withdraw(double amount) throws NegativeException {
		if (balance < amount ) {
			throw new NegativeException();
		}
		else {
			balance -= amount;
		}
	}
}