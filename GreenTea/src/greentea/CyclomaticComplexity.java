package greentea;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
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

/**
 * Calculate cyclomatic complexity of given method
 * @author Dongwon Kim and Namgyu Park
 *
 */
public class CyclomaticComplexity {
	cyclomaticVisitor treeVisitor;
	MethodDeclaration methodRepresent;
	CompilationUnit classRepresent;
	String classSource_;
	char[] classSource;

	/**
	 * Constructor used for testing
	 * @param testSource target test source, String of class code
	 * @param methodName target method name
	 */
	public CyclomaticComplexity(String testSource, String methodName) {
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

	/**
	 * Constructor
	 * @param projectName Target project name
	 * @param packageName Target package name
	 * @param className Target class name
	 * @param methodName Target method name
	 */
	public CyclomaticComplexity(String projectName, String packageName, String className, String methodName) {
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
	 * @return Calculated cyclomatic complexity
	 */
	public int getResult() {
		treeVisitor = new cyclomaticVisitor(classSource_);
		methodRepresent.accept(treeVisitor);
		return treeVisitor.cyMetric;
	}
}

/**
 * @author Dongwon Kim and Namgyu Park
 *
 */
class cyclomaticVisitor extends ASTVisitor {
	public int cyMetric = 1;
	public String code = null;

	/**
	 * @param x String of code
	 */
	public cyclomaticVisitor(String x) {
		// TODO Auto-generated constructor stub

		code = x;
	}

	// Block that cyclomatic dosent have to care about : because cyclomatic is
	// calculated at method level
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AnonymousClassDeclaration)
	 */
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TypeDeclaration)
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AnnotationTypeDeclaration)
	 */
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		return false;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.EnumDeclaration)
	 */
	@Override
	public boolean visit(EnumDeclaration node) {
		return false;
	}

	// Block that could create new branch or contains &&, ||
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ConditionalExpression)
	 */
	@Override
	public boolean visit(ConditionalExpression node) {
		cyMetric++;
		findConditionOperator(node.getExpression()); // if(expression){}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.DoStatement)
	 */
	@Override
	public boolean visit(DoStatement node) {
		cyMetric++;
		findConditionOperator(node.getExpression()); // do{}while(expression)
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.CatchClause)
	 */
	@Override
	public boolean visit(CatchClause node) {
		cyMetric++; // catch(exception){}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SwitchCase)
	 */
	@Override
	public boolean visit(SwitchCase node) {
		if (!node.isDefault()) {
			cyMetric++; // increase cyMetric except default case
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ForStatement)
	 */
	@Override
	public boolean visit(ForStatement node) {
		cyMetric++;
		findConditionOperator(node.getExpression()); // for( _ ; expression ; _)
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.IfStatement)
	 */
	@Override
	public boolean visit(IfStatement node) {
		cyMetric++;
		findConditionOperator(node.getExpression()); // if(expression) {} else {}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.EnhancedForStatement)
	 */
	@Override
	public boolean visit(EnhancedForStatement node) {
		cyMetric++;
		findConditionOperator(node.getExpression()); // for( _ : expression)
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.VariableDeclarationFragment)
	 */
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		findConditionOperator(node.getInitializer()); // {type} {varname} = initializer;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.WhileStatement)
	 */
	@Override
	public boolean visit(WhileStatement node) {
		cyMetric++;
		findConditionOperator(node.getExpression());
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ExpressionStatement)
	 */
	@Override
	public boolean visit(ExpressionStatement node) {
		findConditionOperator(node.getExpression());
		return true;
	}

	/**
	 * Find conditional operator in given expression, then add number of operator found to Cyclometic complexity.
	 * @param codeBody Expression to search.
	 */
	public void findConditionOperator(Expression codeBody) {
		if (codeBody == null || code == null)
			return;

		int startIdx = codeBody.getStartPosition();
		char[] charCode = code.substring(startIdx, startIdx + codeBody.getLength()).toCharArray();
		for (int i = 0; i < charCode.length; i++) {
			if (charCode[i] == '&' || charCode[i] == '|') {
				if (i + 1 < charCode.length) {
					if (charCode[i + 1] == charCode[i])
						cyMetric++;
				}
			}
		}
	}
}

/**
 * @author Dongwon Kim and Namgyu Park
 *
 */
class MethodVisitor extends ASTVisitor {
	List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodDeclaration)
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		methodList.add(node);
		return super.visit(node);
	}

	/**
	 * @return List of methods
	 */
	public List<MethodDeclaration> getMethods() {
		return methodList;
	}
}
