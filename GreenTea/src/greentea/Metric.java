package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.*;


public class Metric {
	public Metric() {
		
	}
	
	static public int measureLOC(IMethod method) {
		// T_S01
		return 0;
	}
	
	static public double measureHalstead(IMethod method) {
		// T_S02
		return 0;
	}
	
	static public int measureCyclomatic(IMethod method) {
		// to T_S03
		return 0;
	}
	
	static public double measureMaintain(IMethod method) {
		return Math.max(0, (171
				- 5.2 * Math.log(Metric.measureHalstead(method))
				- 0.23 * Metric.measureCyclomatic(method)
				- 16.2 * Math.log(Metric.measureLOC(method))
				) * 100 / 171 );
	}
}
