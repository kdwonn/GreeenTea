package greentea.test;

import static org.junit.Assert.*;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import greentea.Metric;

@RunWith(SWTBotJunit4ClassRunner.class)
public class UiTest {
	private static SWTWorkbenchBot bot;

	@AfterClass
	public static void afterClass() {
		bot.sleep(2000);
		bot.resetWorkbench();
	}
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		
		bot.menu("Window").menu("Show View", "Other...").click();
		SWTBotShell showView = bot.shell("Show View");
        showView.activate();
        SWTBotTree tree = bot.tree();
        SWTBotTreeItem node = tree.expandNode("Green Tea");
        node.select("Green Tea");
      	bot.button("Open").click();
		
		bot.menu("File").menu("Open Projects from File System...").click();
//		SWTBotShell shell = bot.shell("Import Projects from File System or Archive");
//		shell.activate();
		//bot.textWithLabel("Import source:").setText("C:\\Users\\SAMSUNG\\Documents\\GitHub\\GreenTea\\GreenTea");
		bot.text(0).setText("C:\\Users\\SAMSUNG\\Documents\\GitHub\\GreenTea\\GreenTea");
		bot.button("Finish").click();
	}
	
	@Test
	public void DhamaCouplingTest() {
		double estimatedValue = 1/22;
		assertEquals(estimatedValue, Metric.measureDhama());
	}
	
	@Test
	public void MartinAfferentCouplingTest() {
		double estimatedValue = 0;
		//assertEquals(estimatedValue, MartinCoupling.testCalcAfferentCoupling());
	}
	
	@Test
	public void MartinEfferentCouplingTest() {
		double estimatedValue = 1;
		//assertEquals(estimatedValue, MartinCoupling.testCalcEfferentCoupling());
	}
	
	@Test
	public void MartinInstabilityTest() {
		double estimatedValue = 1;
		//assertEquals(estimatedValue, MartinCoupling.testInstability());
	}
	/*
	 * Test case for checking tutorial open button
	 * Tutorial open button is placed in the toolbar of this plugin.
	 */ 
	@Test 
	public void testOpenTutorial() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		bot.tree().expandNode("Green Tea").getNode("Green Tea").select();
		bot.button("Open").click();
		
		SWTBotView view = bot.viewByTitle("Green Tea");
		assertEquals("Open Tutorial", view.getToolbarButtons().get(0).getToolTipText());
		view.getToolbarButtons().get(0).click();
		view.bot().toolbarButton().click();
		
		assertEquals("Tutorial", bot.activeView().getTitle());
	}
	
	/*
	 * Test case for checking text report function and creating report md file.
	 * Report button is placed in the toolbar of this plugin.
	 */
	@Test
	public void testMakeTextReport() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		bot.tree().expandNode("Green Tea").getNode("Green Tea").select();
		bot.button("Open").click();
		
		SWTBotView view = bot.viewByTitle("Green Tea");
		assertEquals("Make Text report", view.getToolbarButtons().get(1).getToolTipText());
		view.getToolbarButtons().get(1).click();
		view.bot().toolbarButton().click();
		
		File report = new File("text_report.md");
		assertTrue(report.exists());
	}
	
	/*
	 * Test case for checking logging metrics function
	 * Log button is placed in the toolbar of this plugin.
	 */
	@Test
	public void testMakeLog() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		bot.tree().expandNode("Green Tea").getNode("Green Tea").select();
		bot.button("Open").click();
		
		SWTBotView view = bot.viewByTitle("Green Tea");
		assertEquals("Make Log file", view.getToolbarButtons().get(2).getToolTipText());
		view.getToolbarButtons().get(2).click();
		view.bot().toolbarButton().click();
		
		File report = new File("metric_log.txt");
		assertTrue(report.exists());
	}
}
