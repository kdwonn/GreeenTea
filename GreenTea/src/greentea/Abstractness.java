package greentea;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;


/**
 * Calculate Martin Abstractness.
 * 
 * Object instantiate : 
 * <pre>
 * 		Abstractness abst = new Abstractness("Target project Name", "Target package Name");
 * </pre>
 * 
 * @author Byun Han Seoup and Nam goong sung gon.
 * @version 1.0
 * @see 	greentea.Metric
 * @see 	org.eclipse.jdt.core
 *
 */
public class Abstractness {
	ICompilationUnit[] classes;

	int totalClassNum=0;
	int abstClassNum=0;
	int interfaceClassNum=0;
	
	public Abstractness(String projectName, String packageName) {
		// TODO Auto-generated constructor stub
		classes = getCompilationUnits(projectName,packageName);
	}
	
	public void countClass() {
		for(ICompilationUnit clas:classes) {
			this.totalClassNum++;
			try {
				IType[] types=clas.getTypes();
				for(IType type:types) {
					if(Flags.isAbstract(type.getFlags()))
						this.abstClassNum++;
					if(Flags.isInterface(type.getFlags()))
						this.interfaceClassNum++;
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get the result of Abstractness.
	 * 
	 * @return	value of Abstractness.
	 */
	public String getResult() {
		this.countClass();
		if(totalClassNum==0)
			return "";
		double result=(abstClassNum+interfaceClassNum)/(double)totalClassNum;
		result = Math.round(result *100d)/100d;
		return String.valueOf(result);
	}	
	
	/**
	 * Copy the code form ProjectAnalyser, cause it defined as private method.
	 */
	private static ICompilationUnit[] getCompilationUnits(String projectName, String packageName) {
		IPackageFragment[] packages = ProjectAnalyser.getPackages(projectName);
		for(IPackageFragment pack : packages) {
			if(pack.getElementName().equals(packageName)) {
				try {
					return pack.getCompilationUnits();
				} catch (JavaModelException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

}
