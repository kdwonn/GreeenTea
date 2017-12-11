package greentea.test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;

public class AnalyserTest {
	
	@Test
	public void getProjectTest() {
		String[] names = greentea.ProjectAnalyser.getProjectNames();
		for(String name : names) {
			assertNotNull(ResourcesPlugin.getWorkspace().getRoot().getProject(name));
		}
	}
	
	@Test
	public void getClassTest() {
		String[] projectNames = greentea.ProjectAnalyser.getProjectNames();
		org.junit.Assume.assumeFalse(projectNames.length == 0);
		String[] packageNames = greentea.ProjectAnalyser.getPackageNames(projectNames[0]);
		org.junit.Assume.assumeFalse(packageNames.length == 0);
		String[] classNames = greentea.ProjectAnalyser.getClassNames(projectNames[0], packageNames[0]);
		org.junit.Assume.assumeFalse(classNames.length == 0);
		
		IJavaProject[] projects = greentea.ProjectAnalyser.getProjects();
		try {
			for (IPackageFragment frag : projects[0].getPackageFragments()) {
				if(frag.getElementName().equals(packageNames[0])) {
					assertNotNull(frag.getCompilationUnit(classNames[0]));
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
