package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Calculate HalsteadVolume.
 * 
 * Object instantiate :
 * 
 * <pre>
 * 		HalsteadVolume hals = new HalsteadVolume("Target project Name", "Target package Name", "Target className", "Target methodName");
 * 		HalsteadVolume hals = new HalsteadVolume("TestSource", "Target methodName"); this is for Test only.
 * </pre>
 * 
 * @author Byun Han Seoup and Kim jae chang
 * @version 1.0
 * @see greentea.Metric
 * @see greentea.ProjectAnalyser
 * @see org.eclipse.jdt.core
 * @see java.util
 *
 */
public class HalsteadVolume {
	MethodDeclaration methodRepresent;
	CompilationUnit classRepresent;
	String classSource_;
	char[] classSource;

	private static final ArrayList<String> keyword = new ArrayList<String>(Arrays.asList("abstract", "continue", "for",
			"new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private",
			"this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws",
			"case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char",
			"final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const",
			"float", "native", "super", "while"));

	private static final String[] ops = { "++", "--", "*", ".", ";", "/", "%", "!", ">", "<", ">=", "<=", "==", ":",
			"{", "}", "(", ")", "[", "]", "<", ">" };

	private static final String arithOps = "++--*.;/%!><>=<==:{}()[]<>";

	private int operatorCNT = 0, operandCNT = 0, operatorDist = 0, operandDist = 0;

	public HalsteadVolume(String testSource, String methodName) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		classSource_ = testSource;
		classSource = classSource_.toCharArray();

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(classSource);
		parser.setResolveBindings(true);

		MethodVisitor methodVisitor = new MethodVisitor();

		classRepresent = (CompilationUnit) parser.createAST(null);
		classRepresent.accept(methodVisitor);

		for (MethodDeclaration temp : methodVisitor.getMethods()) {
			if (methodName.equals(temp.getName().toString())) {
				methodRepresent = temp;
				break;
			}
		}
	}

	public HalsteadVolume(String projectName, String packageName, String className, String methodName) {
		// TODO Auto-generated constructor stub
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		classSource_ = ProjectAnalyser.getSourceCode(projectName, packageName, className);
		classSource = classSource_.toCharArray();
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(classSource);
		parser.setResolveBindings(true);

		MethodVisitor methodVisitor = new MethodVisitor();

		classRepresent = (CompilationUnit) parser.createAST(null);
		classRepresent.accept(methodVisitor);

		for (MethodDeclaration temp : methodVisitor.getMethods()) {
			if (methodName.equals(temp.getName().toString())) {
				methodRepresent = temp;
				break;
			}
		}
	}

	/**
	 * Get Number of matched string in source code.
	 * 
	 * @param source
	 *            source code
	 * @param findStr
	 *            string to find.
	 * @return count Number of string matched with "findStr" in source.
	 */
	public int countWord(String source, String findStr) {
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {
			lastIndex = source.indexOf(findStr, lastIndex);

			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}

	/**
	 * Counting up operatorDist, operatorCNT
	 * 
	 * @param source
	 *            source code
	 */
	public void arithOpCheck(String source) {
		for (int i = 0; i < ops.length; i++) {
			if (source.contains(ops[i])) {
				operatorDist++;
				operatorCNT += countWord(source, ops[i]);
			}
		}
	}

	/**
	 * Get Source code that remove string warped with " ". ex)
	 * system.out.println("this string will be removed") => system.out.println()
	 * 
	 * @param source
	 *            source code
	 * @return source code, removed string warped with " ".
	 */
	public String rmStringOperand(String source) {
		String[] StrOperandRemoved = source.split("\"");
		String rmStringOperandSource = "";

		for (int i = 0; i < StrOperandRemoved.length; i++) {
			if (i % 2 == 1)
				operandCNT++;
			else
				rmStringOperandSource += StrOperandRemoved[i];
		}
		return rmStringOperandSource;
	}

	/**
	 * Counting up opertorDist, operatorCNT, operandDist, operandCNT
	 * 
	 * @param StrTokens
	 *            Tokens of source code that are divided by " ".
	 */
	public void classifyKeword(StringTokenizer StrTokens) {
		ArrayList<String> optContainer = new ArrayList<String>();
		ArrayList<String> opdContainer = new ArrayList<String>();
		String nToken = null;

		while (StrTokens.hasMoreTokens()) {
			nToken = StrTokens.nextToken();
			if (keyword.contains(nToken)) {
				if (!optContainer.contains(nToken)) {
					operatorDist++;
					optContainer.add(nToken);
				}
				operatorCNT++;
			} else {
				if (!opdContainer.contains(nToken)) {
					operandDist++;
					opdContainer.add(nToken);
				}
				operandCNT++;
			}
		}
	}

	/**
	 * Get the result of HalsteadVolume.
	 * 
	 * @return value of HalsteadVolume
	 */
	public double getResult() {
		String source = this.methodRepresent.toString();

		this.arithOpCheck(source);

		String tmpSource1 = this.rmStringOperand(source);
		StringTokenizer StrTokens = new StringTokenizer(tmpSource1, HalsteadVolume.arithOps + ", \n");
		this.classifyKeword(StrTokens);

		double result = (operatorCNT + operandCNT) * Math.log(operandDist + operatorDist);
		return Math.round(result * 100d) / 100d;
	}

}
