package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;


/**
 * Calculate Martin Coupling metric and instability.
 * 
 * Object instantiate : 
 * <pre>
 * 		MartinCoupling mc = new MartinCoupling("Target project Name", "Target package Name");
 * </pre>
 * 
 * @author Park Junsu and Kim Dongwon
 * @version 1.0
 * @see 	greentea.Metric
 * @see 	greentea.ProjectAnalyser
 * @see 	org.eclipse.jdt.core
 *
 */
public class MartinCoupling {
	private int afferentCoupling;
	private int efferentCoupling;
	private double instability;

	/**
	 * Constructor of MartinCoupling class.
	 * 
	 * @param proj	Project name of the target package 
	 * @param pckg	Package name of the target package
	 */
	public MartinCoupling(String proj, String pckg) {
		
		IPackageFragment targetPackage = null;
		List<IPackageFragment> packages = Arrays.asList(ProjectAnalyser.getPackages(proj));
		
		for(IPackageFragment pack : packages) {
			if(pack.getElementName().equals(pckg)) {
				targetPackage = pack;
			}
		}
		
		afferentCoupling = CalcAfferentCoupling(targetPackage);
		efferentCoupling = CalcEfferentCoupling(targetPackage);
		if(afferentCoupling + efferentCoupling == 0) {
			instability = 0;
		}
		else {
			instability = efferentCoupling / (afferentCoupling + efferentCoupling);
		}
	}
	
	/**
	 * Add all packages on the target project on scope.
	 * 
	 * @param proj 		Project name of the target package
	 * @param scope		List of packages to setting a search scope
	 * @throws JavaModelException
	 */
	public static void addPackagetoScope(IJavaProject proj, List<IPackageFragment> scope) throws JavaModelException {
		List<IPackageFragment> packages = Arrays.asList(proj.getPackageFragments());
		for(IPackageFragment pack : packages) {
			if(pack.getKind() != IPackageFragmentRoot.K_BINARY) {
				scope.add(pack);
			}
		}
	}
	
	/**
	 * Get a another packages of target package's project
	 * 
	 * @param pckg	Target package
	 * @return	All packages of target package's project except target package
	 * @throws JavaModelException
	 */
	public static List<IPackageFragment> getOuterPackages(IPackageFragment pckg) throws JavaModelException {
		IJavaProject rootProject = (IJavaProject) pckg.getAncestor(IJavaElement.JAVA_PROJECT);
		List<IPackageFragment> outerPackages = new ArrayList<IPackageFragment>();
		
		addPackagetoScope(rootProject, outerPackages);
		
		IProject[] reference = rootProject.getProject().getReferencingProjects();
		if((reference != null) && (reference.length > 0)) {
			for (IProject ref : reference) {
				IJavaProject next = JavaCore.create(ref);
				if(next != null) {
					addPackagetoScope(next, outerPackages);
				}
				
			}
		}
		outerPackages.remove(pckg);
		return outerPackages;
	}

	private int CalcAfferentCoupling(IPackageFragment targetPackage) {
		int result = 0;
		
		try {
			SearchEngine searchEngine = new SearchEngine();
			IJavaSearchScope scope = SearchEngine.createJavaSearchScope(getOuterPackages(targetPackage).toArray(new IJavaElement[] {}));
			SearchPattern pattern = SearchPattern.createPattern(targetPackage, IJavaSearchConstants.REFERENCES);
			AfferentRequestor requestor = new AfferentRequestor(targetPackage);
			SearchParticipant[] participant = new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
			searchEngine.search(pattern, participant, scope, requestor, null);
			result = requestor.getResult();
		} catch (CoreException e) {
			e.printStackTrace();
			result = -1;
		}
		
		return result;
	}

	private int CalcEfferentCoupling(IPackageFragment targetPackage) {
		int result = 0;

		try {
			SearchEngine searchEngine = new SearchEngine();
			IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {targetPackage});
			SearchPattern pattern = SearchPattern.createPattern("*", IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
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

	/**
	 * Get the Afferent Coupling value of target package.
	 * 
	 * @return 	Afferent Coupling value
	 */
	public int getCa() {
		return afferentCoupling;
	}
	
	/**
	 * Get the Efferent Coupling value of target package.
	 * 
	 * @return 	Efferent Coupling value
	 */
	public int getCe() {
		return efferentCoupling;
	}
	
	/**
	 * Get the instability of target package
	 * 
	 * @return	Instability value
	 */
	public double getInstability() {
		return instability;
	}
	
	/**
	 * SearchEngine requestor for calculating Afferent Coupling.  
	 * 
	 * Object instantiate : 
	 * <pre>
	 * 		AfferentRequestor request = new AfferentRequestor(targetPackage)
	 * </pre>
	 * 
	 * @author Park Junsu and Kim Dongwon
	 * @version 1.0
	 * @see 	org.eclipse.jdt.core.search.SearchRequestor
	 *
	 */
	public static class AfferentRequestor extends SearchRequestor {
		private int result = 0;
		private IJavaSearchScope target;
		private Set<String> results = null;
		private Set<String> packages = null;

		/**
		 * Constructor for AfferentRequestor.
		 * 
		 * @param targetPackage		Target package object for search
		 */
		public AfferentRequestor(IPackageFragment targetPackage) {
			target = SearchEngine.createJavaSearchScope(new IJavaElement[] { targetPackage });
		}

		/**
		 * Get the number of search results.
		 * 
		 * @return	Number of search results
		 */
		public int getResult() {
			return result;
		}

		/**
		 * Setting before search.
		 */
		@Override
		public void beginReporting() {
			results = new HashSet<String>();
			packages = new HashSet<String>();
		}

		/**
		 * If search matches, add on result.
		 * 
		 * @param match		Matched objects
		 * @see 	org.eclipse.jdt.core.search
		 */
		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			IJavaElement enclosingElement = (IJavaElement) match.getElement();
			if ((enclosingElement != null) && (!target.encloses(enclosingElement))) {
				IJavaElement pkg = enclosingElement.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
				results.add(match.getResource().getFullPath().toString());
				packages.add(pkg.getElementName());
			}
		}
		
		/**
		 * Get a results.
		 */
		public void endReporting() {
			result = results.size();
		}
	}
	
	/**
	 * SearchEngine requestor for calculating Efferent Coupling.  
	 * 
	 * Object instantiate : 
	 * <pre>
	 * 		EfferentRequestor request = new EfferentRequestor()
	 * </pre>
	 * 
	 * @author Park Junsu and Kim Dongwon
	 * @version 1.0
	 * @see 	org.eclipse.jdt.core.search.SearchRequestor
	 *
	 */
	public static class EfferentRequestor extends SearchRequestor {
		int result = 0;
		Set<String> results = null;
		Set<String> packages = null;

		public EfferentRequestor() {
		}

		/**
		 * Get the number of search results.
		 * 
		 * @return	Number of search results
		 */
		public int getResult() {
			return result;
		}

		/**
		 * Setting before search.
		 */
		@Override
		public void beginReporting() {
			results = new HashSet<String>();
			packages = new HashSet<String>();
		}

		private String getPackageName(IJavaElement enclosingElement, int start, int end) {
			if (enclosingElement.getElementType() == IJavaElement.IMPORT_DECLARATION) {
				String name = enclosingElement.getElementName();
				int lastDot = name.lastIndexOf('.');
				return name.substring(0, lastDot);
			}

			ICompilationUnit unit = (ICompilationUnit) enclosingElement.getAncestor(IJavaElement.COMPILATION_UNIT);

			try {
				String source = unit.getSource();
				return source.substring(start, end);
			} catch (JavaModelException e) {
				return null;
			}
		}

		/**
		 * If search matches, add on result.
		 * 
		 * @param match		Matched objects
		 * @see 	org.eclipse.jdt.core.search
		 */
		@Override
		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			int start = match.getOffset();
			int end = start + match.getLength();
			
			if (match.getElement() != null) {
				try {
					String name = getPackageName((IJavaElement) match.getElement(), start, end);
					
					if (!name.startsWith("java")) {
						results.add(match.getResource().getFullPath().toString());
						packages.add(name);
					}
				} catch (StringIndexOutOfBoundsException x) {
					
				}
			}
		}

		/**
		 * Get a results.
		 */
		@Override
		public void endReporting() {
			result = results.size();
		}
	}
}