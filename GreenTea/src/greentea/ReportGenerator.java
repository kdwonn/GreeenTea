package greentea;

import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;

public class ReportGenerator {
	public String generateMarkdownString() {
		StringBuilder sb = new StringBuilder().append(new Heading("Metric Report", 1)).append("\n");
		
		Table.Builder tableBuilder = new Table.Builder().withAlignment(Table.ALIGN_LEFT);
		//TODO : build table
		
		sb = sb.append(tableBuilder.build()).append("\n");
		return sb.toString();
	}
}
