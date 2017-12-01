package greentea.test;

import org.junit.Test;
import static org.junit.Assert.*;
import testexample.SimpleTest;
import org.eclipse.jdt.core.*;

import greentea.GreenTea;
import greentea.MartinCoupling;
import greentea.Metric;

public class MetricTest {
	@Test
	public void DhamaCouplingTest() {
		double estimatedValue = 1/22;
		assertEquals(estimatedValue, Metric.measureDhama());
	}
	
	@Test
	public void MartinAfferentCouplingTest() {
		double estimatedValue = 0;
		assertEquals(estimatedValue, MartinCoupling.testCalcAfferentCoupling());
	}
	
	@Test
	public void MartinEfferentCouplingTest() {
		double estimatedValue = 1;
		assertEquals(estimatedValue, MartinCoupling.testCalcEfferentCoupling());
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