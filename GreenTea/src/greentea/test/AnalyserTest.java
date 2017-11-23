package greentea.test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.junit.Test;

public class AnalyserTest {
	/*@Test
	public void getProjectTest() {
		String[] names = greentea.ProjectAnalyser.getProjectNames();
		for(String name : names) {
			assertNotNull(ResourcesPlugin.getWorkspace().getRoot().getProject(name));
		}
	}
	
	@Test
	public void getClassTest() {
		String[] projectNames = greentea.ProjectAnalyser.getProjectNames();
		org.junit.Assume.assumeNotNull(projectNames);
		String[] packageNames = greentea.ProjectAnalyser.getPackageNames(projectNames[0]);
		org.junit.Assume.assumeNotNull(packageNames);
		String[] classNames = greentea.ProjectAnalyser.getClassNames(projectNames[0], packageNames[0]);
		org.junit.Assume.assumeNotNull(packageNames);
		
		IJavaProject[] projects = greentea.ProjectAnalyser.getProjects();
		for (IPackageFragment frag : projects[0].getPackageFragments()) {
			if(frag.getElementName().equals(packageNames[0])) {
				assertNotNull(frag.getCompilationUnit(classNames[0]));
			}
		}
	}*/
}
