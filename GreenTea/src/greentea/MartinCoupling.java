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

	public int getResult() {
		if(afferentCoupling + efferentCoupling == 0) {
			return 0;
		}
		return efferentCoupling / (afferentCoupling + efferentCoupling);
	}
	
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
	
	/*
	 * Test testexample.Bank with Account.java 
	 */
	public static int testCalcAfferentCoupling() {
		int result = 0;
		List<String> innerClassesName = new ArrayList<String>();
		
		//precondition : innerClasses = "Bank"
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
					String outersrc = MartinCoupling.removeComment(MartinCoupling.testoutersrc);
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
}