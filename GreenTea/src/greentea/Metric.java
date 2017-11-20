package greentea;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

public class Metric {
	static public int measureCyclomatic
	(String projectName, String packageName, String className, String methodName) {
		
		class cyclomaticVisitor extends ASTVisitor{
			int cyMetric = 1;
			String code = null;
			public cyclomaticVisitor(String x) {
				// TODO Auto-generated constructor stub
				code = x;
			}
			//Block that cyclomatic dosent have to care about : because cyclomatic is calculated at method level
			@Override
			public boolean visit(AnonymousClassDeclaration node) {
				return false; 
			}
			@Override
			public boolean visit(TypeDeclaration node) {
				return false;
			}
			@Override
			public boolean visit(AnnotationTypeDeclaration node) {
				return false;
			}
			@Override
			public boolean visit(EnumDeclaration node) {
				return false;
			}
			//Block that could create new branch or contains &&, ||
			@Override
			public boolean visit(ConditionalExpression node) {
				cyMetric++;
				findConditionOperator(node.getExpression()); // if(expression){}
				return true;
			}
			@Override
			public boolean visit(DoStatement node) {
				cyMetric++;
				findConditionOperator(node.getExpression()); // do{}while(expression)
				return true;
			}
			@Override
			public boolean visit(CatchClause node) {
				cyMetric++; // catch(exception){}
				return true;
			}
			
			public void findConditionOperator(Expression codeBody) {
				int startIdx = codeBody.getStartPosition();
				char[] charCode = code.substring(startIdx, startIdx + codeBody.getLength()).toCharArray();
				for(int i = 0; i < code.length(); i ++) {
					if(charCode[i] == '&' || charCode[i] == '|') {
						if (charCode[i + 1] == charCode [i]) cyMetric ++;
					}
				}
			}
		};
		class MethodVisitor extends ASTVisitor {
		    List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();

		    @Override
		    public boolean visit(MethodDeclaration node) {
		        methodList.add(node);
		        return super.visit(node);
		    }

		    public List<MethodDeclaration> getMethods() {
		        return methodList;
		    }
		};
		
		cyclomaticVisitor treeVisitor;
		MethodDeclaration methodRepresent;
		MethodVisitor methodVisitor = new MethodVisitor();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String classSource_ = ProjectAnalyser.getSourceCode(projectName, packageName, className);
		char[] classSource = classSource_.toCharArray();
		
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(classSource);
		parser.setResolveBindings(true);
		
		CompilationUnit classRepresent = (CompilationUnit)parser.createAST(null);
		classRepresent.accept(methodVisitor);
		
		for(MethodDeclaration temp : methodVisitor.getMethods()) {
			if(methodName.equals(temp.getName().toString())) {
				methodRepresent = temp;
				break;
			}
		}
		
		
		return 0;
	}
	static public double measureDhama
	(String projectName, String packageName, String className, String methodName) {
		return 0;
	}
}
