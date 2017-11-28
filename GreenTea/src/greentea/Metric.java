package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.eclipse.jdt.core.*;




public class Metric {
	public Metric() {

	}

	static public int measureLOC(IMethod method) {
		// T_S01
		return 0;
	}

	static public double measureHalstead(String projectName, String packageName, String className, String methodName) {
		// T_S02
		HalsteadVolume hals = new HalsteadVolume(projectName, packageName, className, methodName);
		return hals.getResult();
	}

	static public int measureCyclomatic(String projectName, String packageName, String className, String methodName) {
		// to T_S03
		CyclomaticComplexity cyclomaticCal = new CyclomaticComplexity(projectName, packageName, className, methodName);
		return cyclomaticCal.getResult();
	}

	/*static public double measureMaintain(IMethod method) {
		return Math.max(0, (171
				- 5.2 * Math.log(Metric.measureHalstead(method))
				- 0.23 * Metric.measureCyclomatic(method)
				- 16.2 * Math.log(Metric.measureLOC(method))
				) * 100 / 171 );
	}
*/
	static public int measureDhama() {
		// to T_S04
		return 0;
	}

	/*static public int measureMartin(String proj, String pckg) {
		MartinCoupling mc = new MartinCoupling(proj, pckg);
		return mc.getResult();
	}*/

	class MartinCoupling {
		private int afferentCoupling;
		private int efferentCoupling;
		List<String> innerClassesName;
		List<String> outerClassesName;

		public MartinCoupling(String proj, String pckg) {
			List<String> projects = Arrays.asList(ProjectAnalyser.getProjectNames());

			if(!projects.contains(proj)) {
				//exception
			}

			List<String> packages = Arrays.asList(ProjectAnalyser.getPackageNames(proj));

			if(!packages.contains(pckg)) {
				//exception
			}

			innerClassesName = Arrays.asList(ProjectAnalyser.getClassNames(proj, pckg));
			outerClassesName = new ArrayList<String>();
			for(String packs : packages) {
				if(!packs.equals(pckg)) {
					outerClassesName.addAll(Arrays.asList(ProjectAnalyser.getClassNames(proj, packs)));
				}
			}
			afferentCoupling = CalcAfferentCoupling(proj, pckg);
			efferentCoupling = CalcEfferentCoupling(proj, pckg);
		}

		private int CalcAfferentCoupling(String proj, String pckg) {
			int result = 0;

			List<String> packages = Arrays.asList(ProjectAnalyser.getPackageNames(proj));

			for(String packs : packages) {
				List<String> outerClasses = Arrays.asList(ProjectAnalyser.getClassNames(proj, packs));
				if(!packs.equals(pckg)) {
					for(String outer : outerClasses) {
						String outersrc = removeComment(ProjectAnalyser.getSourceCode(proj, packs, outer));
						for(String inner : innerClassesName) {
							if(outersrc.matches("[^]*[^a-zA-Z0-9]"+inner+"[^a-zA-Z0-9][^]*")) {
								result++;
								break; 
							}
						}
					}
				}
			}
			return result;
		}

		private int CalcEfferentCoupling(String proj, String pckg) {
			int result = 0;

			for(String inner : innerClassesName) {
				String innersrc = removeComment(ProjectAnalyser.getSourceCode(proj, pckg, inner));
				for(String outer : outerClassesName) {
					if(innersrc.matches("[^]*[^a-zA-Z0-9]"+outer+"[^a-zA-Z0-9][^]*")) {
						result++;
						break; 
					}
				}
			}
			return result;
		}

		private String removeComment(String src) {
			int idxMultiLineComment = src.trim().indexOf("/*");
			int idxSingleLineComment = src.trim().indexOf("//");
			String result = src;

			while ((idxMultiLineComment == 0) || (idxSingleLineComment == 0)) {
				if (idxMultiLineComment == 0) {
					result = src.substring(src.indexOf("*/") + 2);
				} else {
					result = src.substring(src.indexOf('\n') + 1);
				} 

				idxMultiLineComment = src.trim().indexOf("/*");
				idxSingleLineComment = src.trim().indexOf("//");
			}
			return result;
		}

		public int getCa() {
			return afferentCoupling;
		}

		public int getCe() {
			return efferentCoupling;
		}

		public int getResult() {
			if(afferentCoupling + efferentCoupling == 0) {
				return 0;
			}
			return efferentCoupling / (afferentCoupling + efferentCoupling);
		}
	}
}

