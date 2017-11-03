package greentea.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import greentea.parts.GreenTea;

public class MetricTest {
	@Test
	public void DhamaCouplingTest() {
		double estimatedValue = 1/22;
		assertEqual(estimatedValue, greentea.MeasureMetric.measureCoupling(new testexample.Bank("bank")));
	}
	
	@Test
	public void MartinCouplingTest() {
		double estimatedValue = 1;
		assertEqual(estimatedValue, greentea.MeasureMetric.measureCoupling(new testexample.Bank("bank")));
	}
}