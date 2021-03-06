package greentea;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;

public class ProjectAnalyser {
	/**
	 * return names of project in workspace
	 * 
	 * @return String[]
	 */
	public static String[] getProjectNames() {
		IJavaProject[] projects = getProjects();
		if (projects == null)
			return new String[0];
		List<String> nameList = new LinkedList<String>();
		for (IJavaProject prj : projects) {
			nameList.add(prj.getElementName());
		}
		return nameList.toArray(new String[] {});
	}

	/**
	 * return names of package in specific project
	 * 
	 * @param projectName
	 *            as String
	 * @return String[]
	 */
	public static String[] getPackageNames(String projectName) {
		IPackageFragment[] packages = getPackages(projectName);
		if (packages == null)
			return new String[0];
		List<String> nameList = new LinkedList<String>();
		for (IPackageFragment pack : packages) {
			nameList.add(pack.getElementName());
		}
		return nameList.toArray(new String[] {});
	}

	/**
	 * return names of class in specific project, package
	 * 
	 * @param projectName
	 *            as String
	 * @param packageName
	 *            as String
	 * @return String[]
	 */
	public static String[] getClassNames(String projectName, String packageName) {
		ICompilationUnit[] classes = getCompilationUnits(projectName, packageName);
		if (classes == null)
			return new String[0];
		List<String> nameList = new LinkedList<String>();
		for (ICompilationUnit pack : classes) {
			nameList.add(pack.getElementName());
		}
		return nameList.toArray(new String[] {});
	}

	/**
	 * return names of method in specific project, package, class
	 * 
	 * @param projectName
	 *            as String
	 * @param packageName
	 *            as String
	 * @param className
	 *            as String
	 * @return String[]
	 */
	public static String[] getMethodNames(String projectName, String packageName, String ClassName) {
		ICompilationUnit compilationUnit = getCompilationUnit(projectName, packageName, ClassName);
		if (compilationUnit == null)
			return new String[0];
		List<String> nameList = new LinkedList<String>();
		try {
			IType[] types = compilationUnit.getAllTypes();
			for (IType type : types) {
				for (IMethod method : type.getMethods()) {
					nameList.add(method.getElementName());
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
			return null;
		}
		return nameList.toArray(new String[0]);
	}

	/**
	 * return sourcecode of class in specific project, package
	 * 
	 * @param projectName
	 *            as String
	 * @param packageName
	 *            as String
	 * @param ClassName
	 *            as String
	 * @return String
	 */
	public static String getSourceCode(String projectName, String packageName, String ClassName) {
		ICompilationUnit compilationUnit = getCompilationUnit(projectName, packageName, ClassName);
		if (compilationUnit == null)
			return null;
		try {
			return compilationUnit.getSource();
		} catch (JavaModelException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * return array of JavaProject contained in current workspace
	 * 
	 * @return IJavaProject[]
	 */
	public static IJavaProject[] getProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IJavaProject> projectList = new LinkedList<IJavaProject>();
		for (IProject prj : projects) {
			IJavaProject javaProj = JavaCore.create(prj);
			if (javaProj != null) {
				projectList.add(javaProj);
			}
		}
		return projectList.toArray(new IJavaProject[] {});
	}

	/**
	 * return array of IPackageFragment in specific project
	 * 
	 * @param projectName
	 *            as String
	 * @return IPackageFragment[]
	 */
	public static IPackageFragment[] getPackages(String projectName) {
		IJavaProject[] projects = getProjects();
		IJavaProject objectProject = null;
		for (IJavaProject prj : projects) {
			if (prj.getElementName().equals(projectName)) {
				objectProject = prj;
				break;
			}
		}
		if (objectProject == null) {
			return new IPackageFragment[0];
		}

		IPackageFragmentRoot[] packageRoots;
		try {
			packageRoots = objectProject.getPackageFragmentRoots();
			List<IPackageFragment> fragmentList = new LinkedList<IPackageFragment>();
			for (IPackageFragmentRoot packRoot : packageRoots) {
				if (packRoot instanceof JarPackageFragmentRoot)
					continue;
				IJavaElement[] fragments = packRoot.getChildren();
				for (IJavaElement fragJE : fragments) {
					IPackageFragment frag = (IPackageFragment) fragJE;
					if (frag.getElementName().isEmpty())
						continue;
					fragmentList.add(frag);
				}
			}
			return fragmentList.toArray(new IPackageFragment[] {});
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}

	}

	private static ICompilationUnit[] getCompilationUnits(String projectName, String packageName) {
		IPackageFragment[] packages = getPackages(projectName);
		for (IPackageFragment pack : packages) {
			if (pack.getElementName().equals(packageName)) {
				try {
					return pack.getCompilationUnits();
				} catch (JavaModelException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return new ICompilationUnit[0];
	}

	private static ICompilationUnit getCompilationUnit(String projectName, String packageName, String ClassName) {
		ICompilationUnit[] classes = getCompilationUnits(projectName, packageName);
		for (ICompilationUnit compilationUnit : classes) {
			if (compilationUnit.getElementName().equals(ClassName)) {
				return compilationUnit;
			}
		}
		return null;
	}

	/**
	 * return IMethod with specific projectname, packagename, classname, and
	 * methodname
	 * 
	 * @param projectName
	 *            as String
	 * @param packageName
	 *            as String
	 * @param className
	 *            as String
	 * @param methodName
	 *            as String
	 * @return IMethod
	 */
	public static IMethod getIMethod(String projectName, String packageName, String className, String methodName) {
		IMethod method = null;
		ICompilationUnit compilationUnit = ProjectAnalyser.getCompilationUnit(projectName, packageName, className);
		try {
			IType[] types = compilationUnit.getAllTypes();
			for (IType type : types) {
				for (IMethod itr : type.getMethods()) {
					if (itr.getElementName().equals(methodName)) {
						method = itr;
					}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return method;
	}
}
