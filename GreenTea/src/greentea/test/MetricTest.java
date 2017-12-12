package greentea.test;

import org.junit.AfterClass;
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
		
		bot.menu("File").menu("Open Projects from File System...").click();
		bot.comboBox(0).setText("C:\\Users\\SAMSUNG\\Documents\\GitHub\\GreenTea\\GreenTea");
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
		assertEquals(estimatedValue, Metric.measureDhama());
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
	
	@Test
	public void HalsteadVolumeTest() {
		double epsilon=0.00001;
		double estimatedValue = 234.58;
		String testSource="public class practice {"
				+"\npublic static void main(String[] args) {"
				+	"\nString data = \"happy new     year   djkdl!\";"
				+	"\nStringTokenizer strToken = new StringTokenizer(data,\" \");"
				+	"\nSystem.out.println(strToken.countTokens());"
				+	"\nwhile(strToken.hasMoreTokens()) {"
				+		"\nSystem.out.println(strToken.nextToken());"
				+	"\n}"
				+	"\nSystem.out.println(\"well done!\");"
				+"\n}"
				+"\n}";
		boolean isSame=Math.abs(estimatedValue-Metric.measureHalsteadWithSource(testSource, "main"))<epsilon;
		assertEquals(false,isSame);
	}
	
	@Test
	public void CyclomaticComplexTest() {
		double estimatedValue = 3;
		String testSource = 
				"\n public class SimpleTest { "
				+"\n 	public SimpleTest () { "
				+"\n 		int a = 3; "
				+"\n 		int b = 2; "
				+"\n 		System.out.print(a+b); "
				+"\n 	} " 	
				+"\n 	public void cycle() { "
				+"\n 		for(int i = 0; i < 3; i++) { "
				+"\n 			if(i > 1) { "
				+"\n 				System.out.print(1); "
				+"\n 			} "
				+"\n 		} "
				+"\n 	} // 32, 20 "
				+"\n } ";
		assertEquals(true, estimatedValue == Metric.measureCyclomaticWithSource(testSource, "cycle"));
	}
	@Test
	public void MaintainIndexTest() {
		double estimatedValue = 85.7;
		IMethod dummy = null;
		assertEquals(estimatedValue, Metric.measureMaintain(dummy, "GreenTea", "testexample.Bank", "SimpleTest", "cycle"));
	}
}