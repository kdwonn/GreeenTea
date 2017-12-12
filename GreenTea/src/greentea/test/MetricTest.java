package greentea.test;

import org.junit.Test;
import static org.junit.Assert.*;
import testexample.SimpleTest;
import org.eclipse.jdt.core.*;

import greentea.GreenTea;
import greentea.Metric;

public class MetricTest {
	@Test
	public void DhamaCouplingTest() {
		double estimatedValue = 1/22;
		assertEquals(estimatedValue, Metric.measureDhama());
	}
	
	@Test
	public void MartinCouplingTest() {
		double estimatedValue = 1;
		assertEquals(estimatedValue, Metric.measureMartin("GreenTea", "testexample.Bank"));
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