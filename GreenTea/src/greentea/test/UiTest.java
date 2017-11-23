package greentea.test;

import static org.junit.Assert.*;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
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
		// init swtbot
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
	}

	@AfterClass
	public static void afterClass() {
		bot.sleep(2000);
		bot.resetWorkbench();
	}
	
	/*
	 * Test case for checking tutorial open button
	 * Tutorial open button is placed in the toolbar of this plugin.
	 */ 
	@Test 
	public void testTutorialOpen() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		bot.tree().expandNode("Green Tea").getNode("Green Tea").select();
		bot.button("Open").click();
		
		SWTBotView view = bot.viewByTitle("Green Tea");
		assertEquals("Open Tutorial", view.getToolbarButtons().get(0).getToolTipText());
		view.getToolbarButtons().get(0).click();
		view.bot().toolbarButton().click();
		
		assertEquals("Tutorial", bot.activeView().getTitle());
	}
	
}
