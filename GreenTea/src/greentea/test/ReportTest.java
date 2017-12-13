package greentea.test;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import greentea.*;

public class ReportTest {
	
	@Test
	public void testReportGenerated() {
		String filename = "metricreport.md";
		File file = new File(filename);
		
		if(file.exists()) {
			org.junit.Assume.assumeTrue(file.delete());
		}
		greentea.ReportGenerator.generateReport(filename);
		
		assertTrue(file.exists());
	}
	
	@Test
	public void testLog() {
		Date date = new Date();
		String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(date);
		String filename = "log-" + time + ".md";
		greentea.Logging.writeLog();
		File file = new File(filename);
		assertTrue(file.exists());
	}
}

