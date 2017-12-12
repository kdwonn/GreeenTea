package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;


public class HalsteadVolume {
	cyclomaticVisitor treeVisitor;
	MethodDeclaration methodRepresent;
	CompilationUnit classRepresent;
	String classSource_;
	char[] classSource;
	
	private static final  ArrayList<String> keyword = new ArrayList<String>(Arrays.asList(
			"abstract","continue","for","new","switch","assert","default","goto","package","synchronized"
			,"boolean","do","if","private","this","break","double","implements","protected","throw","byte"
			,"else","import","public","throws","case","enum","instanceof","return","transient","catch","extends"
			,"int","short","try","char","final","interface","static","void","class","finally","long","strictfp"
			,"volatile","const","float","native","super","while"
			));

	private static final String[] ops = {
			"++","--","*",".",";","/","%","!",">","<",">=","<=","==",":"
			,"{","}","(",")","[","]","<",">"};

	private static final String arithOps="++--*.;/%!><>=<==:{}()[]<>";

	private int operatorCNT =0, operandCNT =0, operatorDist =0, operandDist=0;
	
	public HalsteadVolume(String testSource, String methodName) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		classSource_ = testSource;
		classSource = classSource_.toCharArray();
		
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(classSource);
		parser.setResolveBindings(true);
		
		MethodVisitor methodVisitor = new MethodVisitor();
		
		classRepresent = (CompilationUnit)parser.createAST(null);
		classRepresent.accept(methodVisitor);
		
		for(MethodDeclaration temp : methodVisitor.getMethods()) {
			if(methodName.equals(temp.getName().toString())) {
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
		
		classRepresent = (CompilationUnit)parser.createAST(null);
		classRepresent.accept(methodVisitor);
		
		for(MethodDeclaration temp : methodVisitor.getMethods()) {
			if(methodName.equals(temp.getName().toString())) {
				methodRepresent = temp;
				break;
			}
		}
	}
	
	public int countWord(String source, String findStr) {
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){
			lastIndex = source.indexOf(findStr,lastIndex);

			if(lastIndex != -1){
				count ++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}

	public void arithOpCheck(String source) {
		for(int i=0; i<ops.length; i++) {
			if(source.contains(ops[i])) {
				operatorDist++;
				operatorCNT+=countWord(source, ops[i]);
			}
		}
	}

	public String rmStringOperand(String source) {
		String[] StrOperandRemoved=source.split("\"");
		String rmStringOperandSource="";

		//System.out.println(StrTokens);
		for(int i=0; i<StrOperandRemoved.length; i++) {
			if(i%2==1)
				operandCNT++;
			else
				rmStringOperandSource+=StrOperandRemoved[i];
		}
		return rmStringOperandSource;
	}

	public void classifyKeword(StringTokenizer StrTokens) {
		ArrayList<String> optContainer = new ArrayList<String>();
		ArrayList<String> opdContainer = new ArrayList<String>();
		String nToken=null;
		
		while(StrTokens.hasMoreTokens()) { 
			nToken = StrTokens.nextToken();
			if(keyword.contains(nToken)) {
				if(!optContainer.contains(nToken)) {
					operatorDist++;
					optContainer.add(nToken);
				}
				operatorCNT++;
			}
			else {
				if(!opdContainer.contains(nToken)) {
					operandDist++;
					opdContainer.add(nToken);
				}
				operandCNT++;
			}
		}
	}
	public double getResult() {
		String source = this.methodRepresent.toString();
		
		this.arithOpCheck(source);
		
		String tmpSource1=this.rmStringOperand(source);
		StringTokenizer StrTokens = new StringTokenizer(tmpSource1,HalsteadVolume.arithOps+", \n");
		this.classifyKeword(StrTokens);
		
		double result = (operatorCNT + operandCNT) * Math.log(operandDist+operatorDist);
		return Math.round(result * 100d)/100d;
	}	

}
