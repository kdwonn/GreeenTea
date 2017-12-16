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
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

import greentea.Metric;
import greentea.ProjectAnalyser;

@RunWith(SWTBotJunit4ClassRunner.class)
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
		for(String projectName:ProjectAnalyser.getProjectNames())
			for(String packageName:ProjectAnalyser.getPackageNames(projectName))
				for(String className:ProjectAnalyser.getClassNames(projectName, packageName)) {
					IJavaProject[] projects = greentea.ProjectAnalyser.getProjects();
					try {
						for (IPackageFragment frag : projects[0].getPackageFragments()) {
							if(frag.getElementName().equals(packageName)) {
								assertNotNull(frag.getCompilationUnit(className));
							}
						}
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
		
	}
}
