package greentea.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

@RunWith(SWTBotJunit4ClassRunner.class)
public class UiTest {
	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		try {
			bot.viewByTitle("Welcome").close();
		} catch (WidgetNotFoundException e) {
		}

		bot.menu("Window").menu("Show View", "Other...").click();
		SWTBotShell showView = bot.shell("Show View");
		showView.activate();
		SWTBotTree tree = bot.tree();
		SWTBotTreeItem node = tree.expandNode("Green Tea");
		node.select("Green Tea");
		bot.button("Open").click();

		Bundle bundle = Platform.getBundle("GreenTea");
		String pluginPath = bundle.getLocation().replaceAll("reference:file:", "");

		pluginPath = pluginPath + "\\..\\testexample";

		bot.menu("File").menu("Open Projects from File System...").click();
		bot.comboBox(0).setText(pluginPath);
		if (bot.button("Finish").isEnabled())
			bot.button("Finish").click();
		else
			bot.button("Cancel").click();
	}

	@AfterClass
	public static void afterClass() {
		bot.sleep(2000);
		bot.resetWorkbench();
	}

	/*
	 * Test case for checking Highlighting in Green Tea
	 */
	@Test
	public void highlightTest() {

		SWTBotView view = bot.viewByTitle("Green Tea");
		view.bot().tree().getTreeItem("GreenTea").expand();
		view.bot().tree().getTreeItem("GreenTea").getNode("greentea").expand();
		view.bot().tree().getTreeItem("GreenTea").getNode("greentea").getNode("GreenTea.java").expand();
		view.bot().tree().getTreeItem("GreenTea").getNode("greentea").getNode("GreenTea.java").getNode("setFocus").doubleClick();
		bot.sleep(500);
		String result = bot.activeEditor().toTextEditor().getSelection();
		String expected = "setFocus";
		assertEquals(expected,result);
		
	}
	
	/*
	 * Test case for checking expandedNode in Green Tea
	 */
	@Test
	public void expnadNodeTest() {
		SWTBotView view = bot.viewByTitle("Green Tea");
		SWTBotTreeItem item = view.bot().tree().getTreeItem("GreenTea");
		item.doubleClick();
		assertTrue(item.isExpanded());
		
		SWTBotTreeItem item2 = view.bot().tree().getTreeItem("GreenTea");
		item2.doubleClick();	
		item2.doubleClick();
		assertNotNull(item2.getNode("greentea"));
		
	}

	/*
	 * Test case for checking tutorial open button Tutorial open button is placed in
	 * the toolbar of this plugin.
	 */
	@Test
	public void testOpenTutorial() {
		bot.button("Tutorial").click();

		assertEquals("tutorial.md", bot.activeEditor().getTitle());

		bot.activeEditor().close();
	}

	/*
	 * Test case for checking text report function and creating report md file.
	 * Report button is placed in the toolbar of this plugin.
	 */
	@Test
	public void testMakeTextReport() {
		bot.button("Report").click();

		assertEquals("metric_report.md", bot.activeEditor().getTitle());

		bot.activeEditor().close();
	}
}
