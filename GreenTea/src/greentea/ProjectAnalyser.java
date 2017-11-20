package greentea;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;

public class ProjectAnalyser {
	/**
	 * return names of project in workspace
	 * @return String[]   
	 */
	public String[] getProjectNames() {
		IJavaProject[] projects = getProjects();
		if(projects == null) return null;
		List<String> nameList = new LinkedList<String>();
		for(IJavaProject prj : projects) {
			nameList.add(prj.getElementName());
		}
		return nameList.toArray(new String[] {});
	}
	
	/**
	 * return names of package in specific project
	 * @param projectName as String
	 * @return String[]
	 */
	public String[] getPackageNames(String projectName) {
		IPackageFragment[] packages = getPackages(projectName);
		if(packages == null) return null;
		List<String> nameList = new LinkedList<String>();
		for(IPackageFragment pack : packages) {
			nameList.add(pack.getElementName());
		}
		return nameList.toArray(new String[] {});
	}
	
	/**
	 * return names of class in specific project, package
	 * @param projectName as String
	 * @param packageName as String
	 * @return String[]
	 */
	public String[] getClassNames(String projectName, String packageName) {
		ICompilationUnit[] classes = getCompilationUnits(projectName, packageName);
		if(classes == null) return null;
		List<String> nameList = new LinkedList<String>();
		for(ICompilationUnit pack : classes) {
			nameList.add(pack.getElementName());
		}
		return nameList.toArray(new String[] {});
	}
	
	/**
	 * return sourcecode of class in specific project, package
	 * @param projectName as String
	 * @param packageName as String
	 * @param ClassName as String
	 * @return String
	 */
	public String getSourceCode(String projectName, String packageName, String ClassName) {
		ICompilationUnit compilationUnit = getCompilationUnit(projectName, packageName, ClassName);
		if(compilationUnit == null) return null;
		try {
			return compilationUnit.getSource();
		} catch (JavaModelException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private IJavaProject[] getProjects() {
		IProject[] projects =  ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IJavaProject> projectList = new LinkedList<IJavaProject>();
		for(IProject prj : projects) {
			IJavaProject javaProj = JavaCore.create(prj);
			if(javaProj != null) {
				projectList.add(javaProj);
			}
		}
		return projectList.toArray(new IJavaProject[] {});
	}
	
	private IPackageFragment[] getPackages(String projectName) {
		IJavaProject[] projects = getProjects();
		IJavaProject objectProject = null;
		for(IJavaProject prj : projects) {
			if(prj.getElementName().equals(projectName)) {
				objectProject = prj;
				break;
			}
		}
		if(objectProject == null) {
			return null;		
		}
		
		IPackageFragmentRoot[] packageRoots;
		try {
			packageRoots = objectProject.getPackageFragmentRoots();
			List<IPackageFragment> fragmentList = new LinkedList<IPackageFragment>();
			for(IPackageFragmentRoot packRoot : packageRoots) {
				if(packRoot instanceof JarPackageFragmentRoot) continue;
	            IJavaElement[] fragments = packRoot.getChildren();
	            for(IJavaElement fragJE : fragments) {
	            	IPackageFragment frag = (IPackageFragment)fragJE;
	            	if(frag.getElementName().isEmpty()) continue;
	            	fragmentList.add(frag);
	            }
			}
			return fragmentList.toArray(new IPackageFragment[] {});
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private ICompilationUnit[] getCompilationUnits(String projectName, String packageName) {
		IPackageFragment[] packages = getPackages(projectName);
		for(IPackageFragment pack : packages) {
			if(pack.getElementName().equals(packageName)) {
				try {
					return pack.getCompilationUnits();
				} catch (JavaModelException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}
	
	private ICompilationUnit getCompilationUnit(String projectName, String packageName, String ClassName) {
		ICompilationUnit[] classes = getCompilationUnits(projectName, packageName);
		for(ICompilationUnit compilationUnit : classes) {
			if(compilationUnit.getElementName().equals(ClassName)) {
				return compilationUnit;
			}
		}
		return null;
	}
	
}
