package testexample.Bank;

import java.util.Map;

import testexample.*;
import java.util.HashMap;
import java.util.Collection;

/**
 * The Bank class has all account information that have been created so far. The
 * account number starts at 100000. When the first person creates an account,
 * his account number is 100000 and the account number of the second person who
 * creates an account is 100001.
 */
public class Bank {
	String name;
	int accNumCounter;
	Map<Integer, Account> accs;
	Map<String, Account> accsByName;

	/**
	 * Creates a new bank with the given name.
	 * 
	 * @param bankname
	 *            the name of bank
	 */
	public Bank(String bankname) {
		name = bankname;
		accNumCounter = 0;
		accs = new HashMap<Integer, Account>();
		accsByName = new HashMap<String, Account>();
	}

	/**
	 * Find account with given account number. You have to raise exception when not
	 * found. Please use NotFoundException we gave you.
	 * 
	 * @param accountNum
	 *            looking account number
	 * @return requested account
	 */
	Account findAccount(int accountNum) throws NotFoundException {
		Account account = accs.get(accountNum);
		if (account == null) {
			throw new NotFoundException();
		}
		return account;
	}

	/**
	 * Find account by owner's name. You have to raise exception when not found.
	 * Please use NotFoundException we gave you. If multiple accounts are found by
	 * the given name, return the account with the lowest account number.
	 * 
	 * @param name
	 *            looking account owner's name
	 * @return requested account
	 */
	Account findAccountByName(String name) throws NotFoundException {
		Account account = accsByName.get(name);
		if (account == null) {
			throw new NotFoundException();
		}
		return account;
	}

	/**
	 * Create a new account with the given owner's name, the type of account. and
	 * initial balance.
	 * 
	 * @param name
	 *            owner name
	 * @param accType
	 *            kind of account
	 * @param initial
	 *            initial balance of account
	 * @return the created account
	 */
	Account createAccount(String name, ACCTYPE accType, double initial) {
		// Not Complete
		Account newAccount;
		if (accType == ACCTYPE.SIMPLE)
			newAccount = new SimpleInterest(accNumCounter, name, initial);
		else
			newAccount = new CompoundInterest(accNumCounter, name, initial);
		accs.put(accNumCounter, newAccount);
		accsByName.put(name, newAccount);
		accNumCounter++;
		return newAccount;
	}

	/**
	 * Find the account which has the highest balance. You have to raise exception
	 * when not found. Please use EmptyException we gave you. If there are multiple
	 * accounts with the same maximum balance, return the account with the lowest
	 * account number.
	 */
	Account maxBalance() throws EmptyException {
		if (accNumCounter == 0) {
			throw new EmptyException();
		}
		double max = -1;
		int maxNum = -1;
		for (int i = 0; i < accNumCounter; i++) {
			try {
				Account acci = findAccount(i);
				if (acci.getBalance() > max) {
					maxNum = i;
					max = acci.getBalance();
				}
			} catch (NotFoundException e) {
			}
		}
		return accs.get(maxNum);
	}

	/**
	 * Transfer the money as much as given amount factor from src account to dst
	 * account. You have to raise exception in case when the balance of src account
	 * is smaller than the amount to be sent. Please use NegativeException we gave
	 * you. You cannot send money from one bank to another.
	 * 
	 * @param src
	 *            the account to send money
	 * @param dst
	 *            the account to receive money
	 * @param amount
	 *            amount of money to send
	 */
	void transfer(Account src, Account dst, double amount) throws NegativeException {
		src.withdraw(amount);
		dst.deposit(amount);
	}

	public static void main(String[] args) {
		int errCtr = 0;
		Bank wb = new Bank("SDBank");

		/**
		 * Test case 1. Checking correctness of simple interest accruement.
		 */
		Account s = wb.createAccount("Thomas", ACCTYPE.SIMPLE, 100000.);
		double expected = 100000.0 * (1 + SimpleInterest.interest * 20);
		s.updateBalance(20);
		double result = s.getBalance();
		if (expected != result) {
			System.out.println("Your implementation is certainly wrong (#1)");
			errCtr++;
		}

		/**
		 * Test case 2. Checking findAccount method's exception raising.
		 */
		try {
			wb.findAccount(100001);
			System.out.println("Your implementation is certainly wrong (#2)");
			errCtr++;
		} catch (NotFoundException e) {
		}

		/**
		 * Test case 3. Checking withdraw method's exception raising.
		 */
		try {
			s.withdraw(9999999999.9);
			System.out.println("Your implementation is certainly wrong (#3)");
			errCtr++;
		} catch (NegativeException e) {
		}

		/**
		 * Test case 4. Checking maxBalance method's exception raising.
		 */
		Bank eb = new Bank("EmptyBank");
		try {
			eb.maxBalance();
			System.out.println("Your implementation is certainly wrong (#4)");
			errCtr++;
		} catch (EmptyException e) {
		}

		if (errCtr > 0) {
			System.out.println(Integer.toString(errCtr) + " error(s) found.");
			return;
		}

		System.out.println("Your implementation looks fine at a glance, but there");
		System.out.println("is no guarantee that your implementation is correct.");
		System.out.println("It is your job to ensure correctness by thorough testing.");
	}

}
