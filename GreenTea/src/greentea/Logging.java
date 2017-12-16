package greentea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {
	/**
	 * generate log with filename consist of current time.
	 */
	public static void writeLog() {
		String report = ReportGenerator.generateMarkdownString();
		Date date = new Date();
		String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(date);
		report.replaceAll("Metric Report", time);
		try {
			File file = new File("log-" + time + ".md");
			FileWriter fw = new FileWriter(file, true);
			fw.write(report);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
