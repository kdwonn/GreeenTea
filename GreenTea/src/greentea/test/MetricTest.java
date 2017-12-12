package greentea.test;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import testexample.SimpleTest;
import org.eclipse.jdt.core.*;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import greentea.GreenTea;
import greentea.MartinCoupling;
import greentea.Metric;

public class MetricTest {
	
	private static SWTWorkbenchBot bot;
	
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
	
	@Test
	public void HalsteadVolumeTest() {
		double epsilon=0.00001;
		double estimatedValue = 234.58;
		boolean isSame=Math.abs(estimatedValue-Metric.measureHalstead("projectName", "packageName", "className", "main"))<epsilon;
		assertEquals(true,isSame);
	}
	
	@Test
	public void CyclomaticComplexTest() {
		double estimatedValue = 3;
		assertEquals(true, estimatedValue == Metric.measureCyclomatic("GreenTea", "testexample", "SimpleTest", "cycle"));
	}
	@Test
	public void MaintainIndexTest() {
		double estimatedValue = 85.7;
		IMethod dummy = null;
		assertEquals(estimatedValue, Metric.measureMaintain(dummy, "GreenTea", "testexample.Bank", "SimpleTest", "cycle"));
	}
}