package greentea.test;

import static org.junit.Assert.*;

import java.io.File;

import org.eclipse.core.runtime.Platform;
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
import org.osgi.framework.Bundle;

import greentea.MartinCoupling;
import greentea.Metric;

@RunWith(SWTBotJunit4ClassRunner.class)
public class UiTest {
	private static SWTWorkbenchBot bot;
	private	static MartinCoupling mc;
	
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
      	
      	Bundle bundle = Platform.getBundle("GreenTea");
		String pluginPath = bundle.getLocation().replaceAll("reference:file:", "");
		
		bot.menu("File").menu("Open Projects from File System...").click();
		bot.comboBox(0).setText(pluginPath);
		bot.button("Finish").click();
		
		mc = new MartinCoupling("GreenTea", "testexample.Bank");
	}
	
	@AfterClass
	public static void afterClass() {
		bot.sleep(2000);
		bot.resetWorkbench();
	}
	
	@Test
	public void DhamaCouplingTest() {
		double estimatedValue = 1/22;
		//assertEquals(estimatedValue, Metric.measureDhama());
	}
	
	@Test
	public void MartinAfferentCouplingTest() {
		double estimatedValue = 0;
		double epsilon=0.00001;
		boolean isSame=Math.abs(estimatedValue-mc.getCa())<epsilon;
		assertEquals(true,isSame);
	}
	
	@Test
	public void MartinEfferentCouplingTest() {
		double estimatedValue = 1;
		double epsilon=0.00001;
		boolean isSame=Math.abs(estimatedValue-mc.getCe())<epsilon;
		assertEquals(true,isSame);
	}
	
	@Test
	public void MartinInstabilityTest() {
		double estimatedValue = 1;
		double epsilon=0.00001;
		boolean isSame=Math.abs(estimatedValue-mc.getInstability())<epsilon;
		assertEquals(true,isSame);
	}
	/*
	 * Test case for checking tutorial open button
	 * Tutorial open button is placed in the toolbar of this plugin.
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
