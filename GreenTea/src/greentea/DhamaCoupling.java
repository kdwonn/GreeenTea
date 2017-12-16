package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import greentea.MartinCoupling.AfferentRequestor;
import greentea.MartinCoupling.EfferentRequestor;

public class DhamaCoupling {
	DhamaVisitor treeVisitor;
	MethodDeclaration methodRepresent;
	IMethod iMethodRepresent;
	CompilationUnit classRepresent;
	String classSource_;
	char[] classSource;

	int param;
	int ce;
	int ca;
	int publicUsed;

	public DhamaCoupling(String code, String methodName) {

	}

	public DhamaCoupling(String projectName, String packageName, String className, String methodName) {
		IPackageFragment currentPackage = null;
		List<IPackageFragment> packages = Arrays.asList(ProjectAnalyser.getPackages(projectName));
		for (IPackageFragment pack : packages) {
			if (pack.getElementName().equals(packageName)) {
				currentPackage = pack;
			}
		}
		iMethodRepresent = ProjectAnalyser.getIMethod(projectName, packageName, className, methodName);

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		classSource_ = ProjectAnalyser.getSourceCode(projectName, packageName, className);
		classSource = classSource_.toCharArray();

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(classSource);
		parser.setResolveBindings(true);

		DhamaMethodVisitor methodVisitor = new DhamaMethodVisitor();

		classRepresent = (CompilationUnit) parser.createAST(null);
		classRepresent.accept(methodVisitor);

		for (MethodDeclaration temp : methodVisitor.getMethods()) {
			if (methodName.equals(temp.getName().toString())) {
				methodRepresent = temp;
				break;
			}
		}

		param = methodRepresent.parameters().size();
		ce = CalcEfferentCoupling(iMethodRepresent, currentPackage);
		ca = CalcAfferentCoupling(iMethodRepresent, currentPackage);
	}

	public double getResult() {
		treeVisitor = new DhamaVisitor();
		methodRepresent.accept(treeVisitor);
		List<FieldDeclaration> fieldList = treeVisitor.getFields();
		int publicref = getPublicFieldRef(iMethodRepresent, fieldList);
		double below = publicref + ce + ca + param;
		if (below == 0)
			return 1;
		else
			return (1 / below);
	}

	private int CalcAfferentCoupling(IMethod targetMethod, IPackageFragment currentPack) {
		int result = 0;
		try {
			SearchEngine searchEngine = new SearchEngine();
			IJavaSearchScope scope = SearchEngine
					.createJavaSearchScope(MartinCoupling.getOuterPackages(currentPack).toArray(new IJavaElement[] {}));
			SearchPattern pattern = SearchPattern.createPattern(targetMethod, IJavaSearchConstants.REFERENCES);
			AfferentRequestor requestor = new AfferentRequestor(currentPack);
			SearchParticipant[] participant = new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
			searchEngine.search(pattern, participant, scope, requestor, null);
			result = requestor.getResult();
		} catch (CoreException e) {
			e.printStackTrace();
			result = -1;
		}
		return result;
	}

	private int CalcEfferentCoupling(IMethod targetMethod, IPackageFragment currentPack) {
		int result = 0;

		try {
			SearchEngine searchEngine = new SearchEngine();
			IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] { targetMethod });
			SearchPattern pattern = SearchPattern.createPattern("*", IJavaSearchConstants.PACKAGE,
					IJavaSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
			EfferentRequestor requestor = new EfferentRequestor();
			SearchParticipant[] participant = new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
			searchEngine.search(pattern, participant, scope, requestor, null);
			result = requestor.getResult();
		} catch (CoreException e) {
			e.printStackTrace();
			result = -1;
		}

		return result;
	}

	private int getPublicFieldRef(IMethod targetMethod, List<FieldDeclaration> fieldList) {
		int result = 0;
		if (fieldList.size() == 0)
			return 0;
		try {
			SearchEngine searchEngine = new SearchEngine();
			IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] { targetMethod });
			SearchPattern pattern = SearchPattern.createPattern((IJavaElement) fieldList.get(0).fragments().get(0),
					IJavaSearchConstants.REFERENCES);
			for (int i = 1; i < fieldList.size(); i++) {
				SearchPattern addedP = SearchPattern.createPattern((IJavaElement) fieldList.get(i).fragments().get(0),
						IJavaSearchConstants.REFERENCES);
				pattern = SearchPattern.createOrPattern(pattern, addedP);
			}
			PublicFieldRequestor requestor = new PublicFieldRequestor();
			SearchParticipant[] participant = new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
			searchEngine.search(pattern, participant, scope, requestor, null);
			result = requestor.getResult();
		} catch (CoreException e) {
			e.printStackTrace();
			result = -1;
		}

		return result;
	}
}

class PublicFieldRequestor extends SearchRequestor {
	private int result = 0;
	private Set<String> results = null;

	public PublicFieldRequestor() {
	}

	public int getResult() {
		return result;
	}

	@Override
	public void beginReporting() {
		results = new HashSet<String>();
	}

	public void acceptSearchMatch(SearchMatch match) throws CoreException {
		IJavaElement enclosingElement = (IJavaElement) match.getElement();
		results.add(enclosingElement.getElementName());
	}

	public void endReporting() {
		result = results.size();
	}
}

class DhamaMethodVisitor extends ASTVisitor {
	List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();

	@Override
	public boolean visit(MethodDeclaration node) {
		methodList.add(node);
		return super.visit(node);
	}

	public List<MethodDeclaration> getMethods() {
		return methodList;
	}
}

class DhamaVisitor extends ASTVisitor {
	List<FieldDeclaration> fieldList = new ArrayList<FieldDeclaration>();

	@Override
	public boolean visit(FieldDeclaration node) {
		if (node.getModifiers() == Modifier.PUBLIC)
			fieldList.add(node);
		return super.visit(node);
	}

	public List<FieldDeclaration> getFields() {
		return fieldList;
	}
}