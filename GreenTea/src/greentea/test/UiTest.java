package greentea.test;

import static org.junit.Assert.*;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class UiTest {
	private static SWTWorkbenchBot bot;
	@BeforeClass
	public static void initBot() {
		// init swtbot
		bot = new SWTWorkbenchBot();
		bot.menu("File").menu("New").menu("Project...").click();
		
		SWTBotShell dialog = bot.shell("New Project");
	    dialog.activate();
	    
	    bot.tree().expandNode("Java").select("Java Project");
	    bot.button("Next >").click();
	    bot.textWithLabel("Project name:").setText("Demo");
	    bot.button("Finish").click();
	    
	    dialog = bot.shell("Open Associated Perspective?");
	    bot.button("No").click();
	}
	@Test
	public void testPulginStart() {
		// Start plugin and check ecisting project appears in tree view
		bot.menu("Window").menu("Show View").menu("Other...").click();
		SWTBotShell dialog = bot.shell("Show View");
	    dialog.activate();
	    bot.tree().expandNode("CSED332").select("Package Overview");
	    bot.button("Open").click();
	    
	    assertEquals("Demo", bot.viewByTitle("GreenTea").bot().tree().getTreeItem("Demo").getText());
	}
	@Test
	public void testAddPackageClass() {
		// Add new package and class in workspace and check tree view is updated
		bot.menu("File").menu("New").menu("Package").click();
	    SWTBotShell dialog = bot.shell("New java Package");
	    dialog.activate();
	    bot.textWithLabel("Source folder:").setText("Demo/src");
	    bot.textWithLabel("Name:").setText("demoPackage");
	    bot.button("Finish").click();
	    
	    bot.menu("File").menu("New").menu("File").click();
	    dialog = bot.shell("New File");
	    dialog.activate();
	    bot.textWithLabel("Enter or select the parent folder:").setText("Demo/src/demoPackage");
	    bot.textWithLabel("File name:").setText("simple.java");
	    bot.button("Finish").click();
	    
	    
		assertEquals("demoPackage", bot.tree().getTreeItem("homework5").getText());
		assertEquals("simple.java", bot.tree().getTreeItem("simple.java").getText());
	}
}
