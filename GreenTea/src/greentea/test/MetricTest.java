package greentea.test;

import org.junit.Test;
import static org.junit.Assert.*;
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
}