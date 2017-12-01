package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MartinCoupling {
	private int afferentCoupling;
	private int efferentCoupling;
	List<String> innerClassesName;
	List<String> outerClassesName;

	public MartinCoupling(String proj, String pckg) {
		List<String> projects = Arrays.asList(ProjectAnalyser.getProjectNames());

		if(!projects.contains(proj)) {
			//exception
		}

		List<String> packages = Arrays.asList(ProjectAnalyser.getPackageNames(proj));

		if(!packages.contains(pckg)) {
			//exception
		}

		innerClassesName = Arrays.asList(ProjectAnalyser.getClassNames(proj, pckg));
		outerClassesName = new ArrayList<String>();
		for(String packs : packages) {
			if(!packs.equals(pckg)) {
				outerClassesName.addAll(Arrays.asList(ProjectAnalyser.getClassNames(proj, packs)));
			}
		}
		afferentCoupling = CalcAfferentCoupling(proj, pckg);
		efferentCoupling = CalcEfferentCoupling(proj, pckg);
	}

	private int CalcAfferentCoupling(String proj, String pckg) {
		int result = 0;

		List<String> packages = Arrays.asList(ProjectAnalyser.getPackageNames(proj));

		for(String packs : packages) {
			if(!packs.equals(pckg)) {
				List<String> outerClasses = Arrays.asList(ProjectAnalyser.getClassNames(proj, packs));
				for(String outer : outerClasses) {
					String outersrc = removeComment(ProjectAnalyser.getSourceCode(proj, packs, outer));
					for(String inner : innerClassesName) {
						if(outersrc.matches("[^]*[^a-zA-Z0-9]"+inner+"[^a-zA-Z0-9][^]*")) {
							result++;
							break; 
						}
					}
				}
			}
		}
		return result;
	}

	private int CalcEfferentCoupling(String proj, String pckg) {
		int result = 0;

		for(String inner : innerClassesName) {
			String innersrc = removeComment(ProjectAnalyser.getSourceCode(proj, pckg, inner));
			for(String outer : outerClassesName) {
				if(innersrc.matches("[^]*[^a-zA-Z0-9]"+outer+"[^a-zA-Z0-9][^]*")) {
					result++;
					break; 
				}
			}
		}
		return result;
	}

	private static String removeComment(String src) {
		int idxMultiLineComment = src.trim().indexOf("/*");
		int idxSingleLineComment = src.trim().indexOf("//");
		String result = src;

		while ((idxMultiLineComment == 0) || (idxSingleLineComment == 0)) {
			if (idxMultiLineComment == 0) {
				result = src.substring(src.indexOf("*/") + 2);
			} else {
				result = src.substring(src.indexOf('\n') + 1);
			} 

			idxMultiLineComment = src.trim().indexOf("/*");
			idxSingleLineComment = src.trim().indexOf("//");
		}
		return result;
	}

	public int getCa() {
		return afferentCoupling;
	}

	public int getCe() {
		return efferentCoupling;
	}

	public int getInstability() {
		if(afferentCoupling + efferentCoupling == 0) {
			return 0;
		}
		return efferentCoupling / (afferentCoupling + efferentCoupling);
	}
	
	/*
	 * Test AfferentCoupling testexample.Bank with Account.java
	 * Expected result = 0
	 */
	public static int testCalcAfferentCoupling() {
		int result = 0;
		List<String> innerClassesName = new ArrayList<String>();
		innerClassesName.add("Bank");
		
		//List<String> packages = Arrays.asList(ProjectAnalyser.getPackageNames(proj));
		List<String> packages = new ArrayList<String>();
		packages.add("testexample");
		packages.add("testexample.Bank");
		
		for(String packs : packages) {
			if(!packs.equals("testexample.Bank")) {
				//List<String> outerClasses = Arrays.asList(ProjectAnalyser.getClassNames(proj, packs));
				List<String> outerClasses = new ArrayList<String>();
				outerClasses.add("Account");
				for(String outer : outerClasses) {
					//String outersrc = removeComment(ProjectAnalyser.getSourceCode(proj, packs, outer));
					String outersrc = MartinCoupling.removeComment(testResources.testoutersrc);
					for(String inner : innerClassesName) {
						if(outersrc.matches("[^]*[^a-zA-Z0-9]"+inner+"[^a-zA-Z0-9][^]*")) {
							result++;
							break; 
						}
					}
				}
			}
		}
		return result;
	}
	
	/*
	 * Test EfferentCoupling testexample.Bank with Account.java
	 * Expected result = 1
	 */
	public static int testCalcEfferentCoupling() {
		int result = 0;
		
		List<String> innerClassesName = new ArrayList<String>();
		List<String> outerClassesName = new ArrayList<String>();
		innerClassesName.add("Bank");
		outerClassesName.add("Account");

		for(String inner : innerClassesName) {
			//String innersrc = removeComment(ProjectAnalyser.getSourceCode(proj, pckg, inner));
			String innersrc = removeComment(testResources.testinnersrc);
			for(String outer : outerClassesName) {
				if(innersrc.matches("[^]*[^a-zA-Z0-9]"+outer+"[^a-zA-Z0-9][^]*")) {
					result++;
					break; 
				}
			}
		}
		return result;
	}
	
	public static int testInstability() {
		
	}
}

class testResources {
	public static String testoutersrc = 
			"package testexample;\r\n" + 
			"\r\n" + 
			"/**\r\n" + 
			" *  This class contains the information needed to represent an account.\r\n" + 
			" *  Account should have information such as owner's name, account number and balance. \r\n" + 
			" */\r\n" + 
			"abstract class Account {\r\n" + 
			"	int accountNumber;\r\n" + 
			"	protected double balance;\r\n" + 
			"	String owner;\r\n" + 
			"\r\n" + 
			"	/**\r\n" + 
			"	 *  This constructor is used when account is created in \"Bank\" object.\r\n" + 
			"	 * \r\n" + 
			"	 *  @param newNumber the new account number \r\n" + 
			"     *  @param name the name of account owner\r\n" + 
			"     *  @param initialFund initial balance when an account is created\r\n" + 
			"	 */	\r\n" + 
			"	Account(int newNumber, String name, double initialFund) {\r\n" + 
			"		accountNumber = newNumber;\r\n" + 
			"		owner = name;\r\n" + 
			"		balance = initialFund;\r\n" + 
			"	}\r\n" + 
			"	\r\n" + 
			"	/**\r\n" + 
			"	 *  This function takes elapsed date and updates account balance accordingly.\r\n" + 
			"	 *  \r\n" + 
			"	 *  @param elapsedDate elapsed date\r\n" + 
			"	 */\r\n" + 
			"	abstract void updateBalance(int elapsedDate);\r\n" + 
			"	\r\n" + 
			"	/**\r\n" + 
			"	 *  Returns the account balance.\r\n" + 
			"	 *  \r\n" + 
			"	 *  @return the balance\r\n" + 
			"	 */\r\n" + 
			"	double getBalance() {\r\n" + 
			"		return balance;\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"	\r\n" + 
			"	/**\r\n" + 
			"	 *  Add as much money as a given amount factor to the given account.\r\n" + 
			"	 *  \r\n" + 
			"	 *  @param amount deposit amount\r\n" + 
			"	 */\r\n" + 
			"	void deposit(double amount) {\r\n" + 
			"		balance += amount;\r\n" + 
			"		//음수인 경우 고려X\r\n" + 
			"	}\r\n" + 
			"	\r\n" + 
			"	/**\r\n" + 
			"	 *  Withdraw as much money as a given amount factor to the given account.\r\n" + 
			"	 *  You have to raise exception in case when the balance of the account\r\n" + 
			"	 *  is smaller than the amount of money that you want to withdraw.\r\n" + 
			"	 *  Please use NegativeException we gave you.\r\n" + 
			"	 *  \r\n" + 
			"	 *  @param amount withdraw amount \r\n" + 
			"	 */\r\n" + 
			"	void withdraw(double amount) throws NegativeException {\r\n" + 
			"		if (balance < amount ) {\r\n" + 
			"			throw new NegativeException();\r\n" + 
			"		}\r\n" + 
			"		else {\r\n" + 
			"			balance -= amount;\r\n" + 
			"		}\r\n" + 
			"	}\r\n" + 
			"}";
	
	public static String testinnersrc = 
			"package testexample;\r\n" + 
			"\r\n" + 
			"import java.util.Map;\r\n" + 
			"import java.util.HashMap;\r\n" + 
			"import java.util.Collection;\r\n" + 
			"\r\n" + 
			"/**\r\n" + 
			" * The Bank class has all account information that have been created so far. The\r\n" + 
			" * account number starts at 100000. When the first person creates an account,\r\n" + 
			" * his account number is 100000 and the account number of the second person who\r\n" + 
			" * creates an account is 100001.\r\n" + 
			" */\r\n" + 
			"public class Bank {\r\n" + 
			"	String name;\r\n" + 
			"	int accNumCounter;\r\n" + 
			"	Map<Integer, Account> accs;\r\n" + 
			"	Map<String, Account> accsByName;\r\n" + 
			"\r\n" + 
			"	/**\r\n" + 
			"	 * Creates a new bank with the given name.\r\n" + 
			"	 * \r\n" + 
			"	 * @param bankname\r\n" + 
			"	 *            the name of bank\r\n" + 
			"	 */\r\n" + 
			"	public Bank(String bankname) {\r\n" + 
			"		name = bankname;\r\n" + 
			"		accNumCounter = 0;\r\n" + 
			"		accs = new HashMap<Integer, Account>();\r\n" + 
			"		accsByName = new HashMap<String, Account>();\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"	/**\r\n" + 
			"	 * Find account with given account number. You have to raise exception when not\r\n" + 
			"	 * found. Please use NotFoundException we gave you.\r\n" + 
			"	 * \r\n" + 
			"	 * @param accountNum\r\n" + 
			"	 *            looking account number\r\n" + 
			"	 * @return requested account\r\n" + 
			"	 */\r\n" + 
			"	Account findAccount(int accountNum) throws NotFoundException {\r\n" + 
			"		Account account = accs.get(accountNum);\r\n" + 
			"		if (account == null) {\r\n" + 
			"			throw new NotFoundException();\r\n" + 
			"		}\r\n" + 
			"		return account;\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"	/**\r\n" + 
			"	 * Find account by owner's name. You have to raise exception when not found.\r\n" + 
			"	 * Please use NotFoundException we gave you. If multiple accounts are found by\r\n" + 
			"	 * the given name, return the account with the lowest account number.\r\n" + 
			"	 * \r\n" + 
			"	 * @param name\r\n" + 
			"	 *            looking account owner's name\r\n" + 
			"	 * @return requested account\r\n" + 
			"	 */\r\n" + 
			"	Account findAccountByName(String name) throws NotFoundException {\r\n" + 
			"		Account account = accsByName.get(name);\r\n" + 
			"		if (account == null) {\r\n" + 
			"			throw new NotFoundException();\r\n" + 
			"		}\r\n" + 
			"		return account;\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"	/**\r\n" + 
			"	 * Create a new account with the given owner's name, the type of account. and\r\n" + 
			"	 * initial balance.\r\n" + 
			"	 * \r\n" + 
			"	 * @param name\r\n" + 
			"	 *            owner name\r\n" + 
			"	 * @param accType\r\n" + 
			"	 *            kind of account\r\n" + 
			"	 * @param initial\r\n" + 
			"	 *            initial balance of account\r\n" + 
			"	 * @return the created account\r\n" + 
			"	 */\r\n" + 
			"	Account createAccount(String name, ACCTYPE accType, double initial) {\r\n" + 
			"		// Not Complete\r\n" + 
			"		Account newAccount;\r\n" + 
			"		if (accType == ACCTYPE.SIMPLE)\r\n" + 
			"			newAccount = new SimpleInterest(accNumCounter, name, initial);\r\n" + 
			"		else\r\n" + 
			"			newAccount = new CompoundInterest(accNumCounter, name, initial);\r\n" + 
			"		accs.put(accNumCounter, newAccount);\r\n" + 
			"		accsByName.put(name, newAccount);\r\n" + 
			"		accNumCounter++;\r\n" + 
			"		return newAccount;\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"	/**\r\n" + 
			"	 * Find the account which has the highest balance. You have to raise exception\r\n" + 
			"	 * when not found. Please use EmptyException we gave you. If there are multiple\r\n" + 
			"	 * accounts with the same maximum balance, return the account with the lowest\r\n" + 
			"	 * account number.\r\n" + 
			"	 */\r\n" + 
			"	Account maxBalance() throws EmptyException {\r\n" + 
			"		if (accNumCounter == 0) {\r\n" + 
			"			throw new EmptyException();\r\n" + 
			"		}\r\n" + 
			"		double max = -1;\r\n" + 
			"		int maxNum = -1;\r\n" + 
			"		for (int i = 0; i < accNumCounter; i++) {\r\n" + 
			"			try {\r\n" + 
			"				Account acci = findAccount(i);\r\n" + 
			"				if (acci.getBalance() > max) {\r\n" + 
			"					maxNum = i;\r\n" + 
			"					max = acci.getBalance();\r\n" + 
			"				}\r\n" + 
			"			} catch (NotFoundException e) {\r\n" + 
			"			}\r\n" + 
			"		}\r\n" + 
			"		return accs.get(maxNum);\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"	/**\r\n" + 
			"	 * Transfer the money as much as given amount factor from src account to dst\r\n" + 
			"	 * account. You have to raise exception in case when the balance of src account\r\n" + 
			"	 * is smaller than the amount to be sent. Please use NegativeException we gave\r\n" + 
			"	 * you. You cannot send money from one bank to another.\r\n" + 
			"	 * \r\n" + 
			"	 * @param src\r\n" + 
			"	 *            the account to send money\r\n" + 
			"	 * @param dst\r\n" + 
			"	 *            the account to receive money\r\n" + 
			"	 * @param amount\r\n" + 
			"	 *            amount of money to send\r\n" + 
			"	 */\r\n" + 
			"	void transfer(Account src, Account dst, double amount) throws NegativeException {\r\n" + 
			"		src.withdraw(amount);\r\n" + 
			"		dst.deposit(amount);\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"	public static void main(String[] args) {\r\n" + 
			"		int errCtr = 0;\r\n" + 
			"		Bank wb = new Bank(\"SDBank\");\r\n" + 
			"\r\n" + 
			"		/**\r\n" + 
			"		 * Test case 1. Checking correctness of simple interest accruement.\r\n" + 
			"		 */\r\n" + 
			"		Account s = wb.createAccount(\"Thomas\", ACCTYPE.SIMPLE, 100000.);\r\n" + 
			"		double expected = 100000.0 * (1 + SimpleInterest.interest * 20);\r\n" + 
			"		s.updateBalance(20);\r\n" + 
			"		double result = s.getBalance();\r\n" + 
			"		if (expected != result) {\r\n" + 
			"			System.out.println(\"Your implementation is certainly wrong (#1)\");\r\n" + 
			"			errCtr++;\r\n" + 
			"		}\r\n" + 
			"\r\n" + 
			"		/**\r\n" + 
			"		 * Test case 2. Checking findAccount method's exception raising.\r\n" + 
			"		 */\r\n" + 
			"		try {\r\n" + 
			"			wb.findAccount(100001);\r\n" + 
			"			System.out.println(\"Your implementation is certainly wrong (#2)\");\r\n" + 
			"			errCtr++;\r\n" + 
			"		} catch (NotFoundException e) {\r\n" + 
			"		}\r\n" + 
			"\r\n" + 
			"		/**\r\n" + 
			"		 * Test case 3. Checking withdraw method's exception raising.\r\n" + 
			"		 */\r\n" + 
			"		try {\r\n" + 
			"			s.withdraw(9999999999.9);\r\n" + 
			"			System.out.println(\"Your implementation is certainly wrong (#3)\");\r\n" + 
			"			errCtr++;\r\n" + 
			"		} catch (NegativeException e) {\r\n" + 
			"		}\r\n" + 
			"\r\n" + 
			"		/**\r\n" + 
			"		 * Test case 4. Checking maxBalance method's exception raising.\r\n" + 
			"		 */\r\n" + 
			"		Bank eb = new Bank(\"EmptyBank\");\r\n" + 
			"		try {\r\n" + 
			"			eb.maxBalance();\r\n" + 
			"			System.out.println(\"Your implementation is certainly wrong (#4)\");\r\n" + 
			"			errCtr++;\r\n" + 
			"		} catch (EmptyException e) {\r\n" + 
			"		}\r\n" + 
			"\r\n" + 
			"		if (errCtr > 0) {\r\n" + 
			"			System.out.println(Integer.toString(errCtr) + \" error(s) found.\");\r\n" + 
			"			return;\r\n" + 
			"		}\r\n" + 
			"\r\n" + 
			"		System.out.println(\"Your implementation looks fine at a glance, but there\");\r\n" + 
			"		System.out.println(\"is no guarantee that your implementation is correct.\");\r\n" + 
			"		System.out.println(\"It is your job to ensure correctness by thorough testing.\");\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"}\r\n" + 
			"";
}