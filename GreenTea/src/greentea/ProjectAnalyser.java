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

class ProjectAnalyser {
	public String[] getProjectNames() {
		IJavaProject[] projects = getProjects();
		List<String> nameList = new LinkedList<String>();
		for(IJavaProject prj : projects) {
			nameList.add(prj.getElementName());
		}
		return nameList.toArray(new String[] {});
	}
	
	public String[] getPackageNames(String projectName) {
		IPackageFragment[] packages = getPackages(projectName);
		List<String> nameList = new LinkedList<String>();
		for(IPackageFragment pack : packages) {
			nameList.add(pack.getElementName());
		}
		return nameList.toArray(new String[] {});
	}
	
	public String[] getClassNames(String projectName, String packageName) {
		ICompilationUnit[] classes = getCompilationUnits(projectName, packageName);
		List<String> nameList = new LinkedList<String>();
		for(ICompilationUnit pack : classes) {
			nameList.add(pack.getElementName());
		}
		return nameList.toArray(new String[] {});
	}
	
	public String getSouceCode(String projectName, String packageName, String ClassName) {
		//TODO
		return null;
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
			// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}
	
}
