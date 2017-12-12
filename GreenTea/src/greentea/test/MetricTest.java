package greentea.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import testexample.SimpleTest;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import greentea.GreenTea;
import greentea.MartinCoupling;
import greentea.Metric;

public class MetricTest {
	
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
		double estimatedValue = 70.6;
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
}