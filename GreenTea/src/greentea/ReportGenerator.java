package greentea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jdt.core.IMethod;

import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;

public class ReportGenerator {
	public static String generateMarkdownString() {
		StringBuilder sb = new StringBuilder().append("Metric Report").append("\n").append("====================").append("\n");
		
		String table = "| Name | Lines of Code | Halstead Volume | Cyclomatec Complexity | Martin's Coupling | Maintainablity Index |\n";
		table = table += "| ------- | ------- | ------- | ------- | ------- | ------- |\n";
		
		for(String projectName:ProjectAnalyser.getProjectNames())
			for(String packageName:ProjectAnalyser.getPackageNames(projectName)) {
				table += String.format("| %s |  |  |  | %s |  |\n", projectName + "." + packageName,
						String.valueOf(Metric.measureMartinInstability(projectName, packageName)));
				for(String className:ProjectAnalyser.getClassNames(projectName, packageName)) {
					table += String.format("| %s | %s |  |  |  |  |\n", projectName + "." + packageName+ "." + className,
							String.valueOf(Metric.measureLOC(projectName, packageName, className)));
					for(String methodName:ProjectAnalyser.getMethodNames(projectName, packageName, className)) {
						IMethod method = ProjectAnalyser.getIMethod(projectName, packageName, className, methodName);
						table += String.format("| %s | %s | %s | %s |  | %s |\n", projectName + "." + packageName + "." + className + "." + methodName,
								String.valueOf(Metric.measureLOC(method)),
								String.valueOf(Metric.measureHalstead(projectName, packageName, className, methodName)),
								String.valueOf(Metric.measureCyclomatic(projectName, packageName, className, methodName)),
								String.valueOf(Metric.measureMaintain(method, projectName, packageName, className, methodName)));
					}
				}
			}
		
		sb = sb.append(table).append("\n");
		return sb.toString();
	}
	
	public static void generateReport(String filename) {
		try {
			File file = new File(filename) ;
			file.deleteOnExit();
			FileWriter fw = new FileWriter(file, true);
			String report = generateMarkdownString();
	        fw.write(report);
	        fw.flush();
	        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
