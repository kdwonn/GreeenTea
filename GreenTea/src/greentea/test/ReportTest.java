package greentea.test;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

import greentea.*;

@RunWith(SWTBotJunit4ClassRunner.class)
public class ReportTest {

	private static SWTWorkbenchBot bot;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		try {
		bot.viewByTitle("Welcome").close();
		}catch(WidgetNotFoundException e) {}
		
		bot.menu("Window").menu("Show View", "Other...").click();
		SWTBotShell showView = bot.shell("Show View");
        showView.activate();
        SWTBotTree tree = bot.tree();
        SWTBotTreeItem node = tree.expandNode("Green Tea");
        node.select("Green Tea");
      	bot.button("Open").click();
      	
      	Bundle bundle = Platform.getBundle("GreenTea");
		String pluginPath = bundle.getLocation().replaceAll("reference:file:", "");
		
		bot.menu("File").menu("Open Projects from File System...").click();
		bot.comboBox(0).setText(pluginPath);
		if(bot.button("Finish").isEnabled())
			bot.button("Finish").click();
		else
			bot.button("Cancel").click();
	}
	
	@AfterClass
	public static void afterClass() {
		bot.sleep(2000);
		bot.resetWorkbench();
	}
	
	@Test
	public void testReportGenerated() {
		String filename = "metricReport.md";
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

