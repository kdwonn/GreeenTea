package greentea.test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

public class AnalyserTest {
	private static SWTWorkbenchBot bot;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		try {
		bot.viewByTitle("Welcome").close();
		}catch(WidgetNotFoundException e) {}
		
		bot.menu("Window").menu("Show View", "Other...").click();
		SWTBotShell showView = bot.shell("Show View");
        showView.activate();
        SWTBotTree tree = bot.tree();
        SWTBotTreeItem node = tree.expandNode("Green Tea");
        node.select("Green Tea");
      	bot.button("Open").click();
      	
      	Bundle bundle = Platform.getBundle("GreenTea");
		String pluginPath = bundle.getLocation().replaceAll("reference:file:", "");
		
		bot.menu("File").menu("Open Projects from File System...").click();
		bot.comboBox(0).setText(pluginPath);
		if(bot.button("Finish").isEnabled())
			bot.button("Finish").click();
		else
			bot.button("Cancel").click();
	}
	
	@AfterClass
	public static void afterClass() {
		bot.sleep(2000);
		bot.resetWorkbench();
	}
	
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
