package greentea.test;

import static org.junit.Assert.*;

import java.io.File;

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

import greentea.Abstractness;
import greentea.MartinCoupling;
import greentea.Metric;

@RunWith(SWTBotJunit4ClassRunner.class)
public class UiTest {
	private static SWTWorkbenchBot bot;
	private	static MartinCoupling mc;
	private static Abstractness abst;
	
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
		
		pluginPath = pluginPath + "\\..\\testexample";
		
		bot.menu("File").menu("Open Projects from File System...").click();
		bot.comboBox(0).setText(pluginPath);
		if(bot.button("Finish").isEnabled())
			bot.button("Finish").click();
		else
			bot.button("Cancel").click();
		
		mc = new MartinCoupling("testexample", "testexample.Bank");
		abst = new Abstractness("testexample", "testexample");
	}
	
	@AfterClass
	public static void afterClass() {
		bot.sleep(2000);
		bot.resetWorkbench();
	}
	@Test
	public void AbstractTest() {
		double estimatedValue = 0.14;
		double epsilon=0.00001;
		boolean isSame = Math.abs(estimatedValue - Double.valueOf(abst.getResult())) <epsilon;
		assertEquals(true, isSame);
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
		bot.button("Report").click();
		
		assertEquals("metric_report.md", bot.activeEditor().getTitle());
		
		bot.activeEditor().close();
	}
}
