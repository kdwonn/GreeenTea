package greentea.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import greentea.Abstractness;
import greentea.MartinCoupling;
import greentea.Metric;

@RunWith(SWTBotJunit4ClassRunner.class)
public class MetricTest {
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
		assertEquals(true,isSame);
	}
	
	@Test
	public void CyclomaticComplexTest() {
		double estimatedValue = 3;
		double estimatedValue2 = 9;
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
		String testSource2 = 
				"public class SimpleTest {"
				+"\n public static IPackageFragment[] getPackages(String projectName) {                    " 
				+"\n		IJavaProject[] projects = getProjects(); " 
				+"\n		IJavaProject objectProject = null; " 
				+"\n		for(IJavaProject prj : projects) { " 
				+"\n			if(prj.getElementName().equals(projectName)) { " 
				+"\n				objectProject = prj; " 
				+"\n				break; " 
				+"\n			} " 
				+"\n		} " 
				+"\n		if(objectProject == null) { " 
				+"\n			return null;		 " 
				+"\n		} " 
				+"\n		IPackageFragmentRoot[] packageRoots; " 
				+"\n		try { " 
				+"\n			packageRoots = objectProject.getPackageFragmentRoots(); " 
				+"\n			List<IPackageFragment> fragmentList = new LinkedList<IPackageFragment>(); " 
				+"\n			for(IPackageFragmentRoot packRoot : packageRoots) { " 
				+"\n				if(packRoot instanceof JarPackageFragmentRoot) continue; " 
				+"\n	            IJavaElement[] fragments = packRoot.getChildren(); " 
				+"\n	            for(IJavaElement fragJE : fragments) { " 
				+"\n	            	IPackageFragment frag = (IPackageFragment)fragJE; " 
				+"\n	            	if(frag.getElementName().isEmpty()) continue; " 
				+"\n	            	fragmentList.add(frag); " 
				+"\n	            } " 
				+"\n			} " 
				+"\n			return fragmentList.toArray(new IPackageFragment[] {}); " 
				+"\n		} catch (CoreException e) { " 
				+"\n			e.printStackTrace(); " 
				+"\n			return null; " 
				+"\n		} " 
				+"\n	} " 
				+"\n}" ;
		assertEquals(true, estimatedValue == Metric.measureCyclomaticWithSource(testSource, "cycle"));
		assertEquals(true, estimatedValue2 == Metric.measureCyclomaticWithSource(testSource2, "getPackages"));
	}
	@Test
	public void MaintainIndexTest() {
		double estimatedValue = 59.1;
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
		Double realValue = (Metric.measureMaintainWithSource(testSource, "cycle")*10);
		realValue = (double)realValue.intValue() / 10;
		assertEquals(true, estimatedValue == realValue);

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
}