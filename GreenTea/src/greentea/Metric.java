package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.eclipse.jdt.core.*;
import org.eclipse.swtbot.swt.finder.utils.StringUtils;

public class Metric {
	public Metric() {

	}
	
	/**
	 * calculate lines of code from sourcecode in string
	 * @param code as String
	 * @return lines of code as int
	 */
	static public int measureLOC(String code) {
		// T_S01
		int count = code.length() - code.replace("\n", "").length();
		return count + 1;
	}
	/**
	 * calculate lines of code for class(.java file) from specific location
	 * @param projectName as String
	 * @param packageName as String
	 * @param className as String
	 * @return lines of code as int
	 */
	static public int measureLOC(String projectName, String packageName, String className) {
		// T_S01
		String code = ProjectAnalyser.getSourceCode(projectName, packageName, className);
		int count = code.length() - code.replace("\n", "").length();
		return count + 1;
	}

	/**
	 * calculate lines of code for method from IMethod
	 * @param method as IMethod
	 * @return lines of code as int
	 */
	static public int measureLOC(IMethod method) {
		String code;
		try {
			code = method.getSource();
			int count = code.length() - code.replace("\n", "").length();
			return count + 1;
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	static public double measureHalstead(String projectName, String packageName, String className, String methodName) {
		// T_S02
		HalsteadVolume hals = new HalsteadVolume(projectName, packageName, className, methodName);
		return hals.getResult();
	}
	static public double measureHalsteadWithSource(String source, String methodName) {
		HalsteadVolume halsteadVolume = new HalsteadVolume(source, methodName);
		return halsteadVolume.getResult();
	}
	static public int measureCyclomatic(String projectName, String packageName, String className, String methodName) {
		// to T_S03
		CyclomaticComplexity cyclomaticCal = new CyclomaticComplexity(projectName, packageName, className, methodName);
		return cyclomaticCal.getResult();
	}
	static public int measureCyclomaticWithSource(String source, String methodName) {
		CyclomaticComplexity cyclomaticCal = new CyclomaticComplexity(source, methodName);
		return cyclomaticCal.getResult();
	}

	static public double measureMaintain(IMethod method, String projectName, String packageName, String className, String methodName) {
		double result= Math.max(0, (171
				- 5.2 * Math.log(Metric.measureHalstead(projectName, packageName, className, methodName))
				- 0.23 * Metric.measureCyclomatic(projectName, packageName, className, methodName)
				- 16.2 * Math.log(Metric.measureLOC(method))
				) * 100 / 171 );
		return Math.round(result * 100d)/100d;
	}
	static public double measureMaintainWithSource(String source, String methodName) {
		double result= Math.max(0, (171
				- 5.2 * Math.log(Metric.measureHalsteadWithSource(source, methodName))
				- 0.23 * Metric.measureCyclomaticWithSource(source, methodName)
				- 16.2 * Math.log(Metric.measureLOC(source))
				) * 100 / 171 );
		return Math.round(result * 100d)/100d;
	}


	static public double measureDhama(String projectName, String packageName, String className, String methodName) {
		// to T_S04
		DhamaCoupling dhama = new DhamaCoupling(projectName, packageName, className, methodName);
		double result = dhama.getResult();
		return Math.round(result * 1000d)/1000d;
	}

	static public int measureMartinAfferent(String proj, String pckg) {
		MartinCoupling mc = new MartinCoupling(proj, pckg);
		return mc.getCa();
	}
	
	static public int measureMartinEfferent(String proj, String pckg) {
		MartinCoupling mc = new MartinCoupling(proj, pckg);
		return mc.getCe();
	}
	
	static public double measureMartinInstability(String proj, String pckg) {
		MartinCoupling mc = new MartinCoupling(proj, pckg);
		return mc.getInstability();
	}
	
	static public String mesureAbstractness(String projectName, String packageName) {
		Abstractness abst = new Abstractness(projectName,packageName);
		return abst.getResult();
	}
}

