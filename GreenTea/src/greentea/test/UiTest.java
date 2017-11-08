package greentea.test;

import static org.junit.Assert.*;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class UiTest {
	private static SWTWorkbenchBot bot;
	@BeforeClass
	public static void initBot() {
		// TODO
		bot = new SWTWorkbenchBot();
		bot.menu("File").menu("New").menu("Project...").click();
		
		SWTBotShell dialog = bot.shell("New Project");
	    dialog.activate();
	    
	    bot.tree().expandNode("Java").select("Java Project");
	    bot.button("Next >").click();
	    bot.textWithLabel("Project name:").setText("Demo");
	    bot.button("Finish").click();
	    
	    dialog = bot.shell("Open Associated Perspective?");
	    bot.button("No").click();
	}
}
