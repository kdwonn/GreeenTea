package greentea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jdt.core.IMethod;

import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;

public class ReportGenerator {
	public String generateMarkdownString() {
		StringBuilder sb = new StringBuilder().append(new Heading("Metric Report", 1)).append("\n");
		
		Table.Builder tableBuilder = new Table.Builder().withAlignment(Table.ALIGN_LEFT);
		tableBuilder = tableBuilder.addRow("Name", "Lines of Code", "Halstead Volume", "Cyclomatic Complexity", "Martin's Coupling", "Maintainability Index");
		
		for(String projectName:ProjectAnalyser.getProjectNames())
			for(String packageName:ProjectAnalyser.getPackageNames(projectName))
				for(String className:ProjectAnalyser.getClassNames(projectName, packageName))
					for(String methodName:ProjectAnalyser.getMethodNames(projectName, packageName, className)) {
						IMethod method = ProjectAnalyser.getIMethod(projectName, packageName, className, methodName);
						tableBuilder = tableBuilder.addRow(projectName + "." + packageName + "." + className + "." + methodName,
								String.valueOf(Metric.measureLOC(method)),
								String.valueOf(Metric.measureHalstead(projectName, packageName, className, methodName)),
								String.valueOf(Metric.measureCyclomatic(projectName, packageName, className, methodName)),
								String.valueOf(Metric.measureMartin(projectName, packageName)),
								String.valueOf(Metric.measureMaintain(method, projectName, packageName, className, methodName)));
					}
		
		sb = sb.append(tableBuilder.build()).append("\n");
		return sb.toString();
	}
	
	public void generateReport(String filename) {
		try {
			File file = new File(filename) ;
			FileWriter fw = new FileWriter(file, true);
	        fw.write(generateMarkdownString());
	        fw.flush();
	        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
