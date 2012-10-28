package org.activityinfo;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.i18n.UIMessages;
import org.junit.Assert;
import org.junit.Test;

public class TranslationTest {

	@Test
	public void constants() throws IOException {
		
		Properties english = TranslationTool.loadTranslations(UIConstants.class, null);
		Properties french = TranslationTool.loadTranslations(UIConstants.class, "fr");
		
		checkForMissingTranslations("UIConstants_fr.properties", english, french);	
	}
	
	@Test
	public void messages() throws IOException {
		Properties english = TranslationTool.loadTranslationsFromInterface(UIMessages.class);
		Properties french = TranslationTool.loadTranslations(UIMessages.class, "fr");
		
		checkForMissingTranslations("UIMessages_fr.properties", english, french);			
	}

	private void checkForMissingTranslations(String file, Properties english,
			Properties french) throws AssertionError {
		boolean missingTranslations = false;
		
		StringBuilder msg = new StringBuilder();
		msg.append("The following translations need to be added to " + file);
		for(Object key : english.keySet()) {
			if(!french.containsKey((String)key)) {
				missingTranslations = true;
				msg.append("\n").append(key).append(" = ").append(english.get(key));
			}
		}
		
		if(missingTranslations) {
			throw new AssertionError(msg.toString());
		}
	}

	@Test
	public void testParseXml() throws Exception {
		UiXmlRefFinder finder = new UiXmlRefFinder(UIConstants.class);
		finder.parseXmlFile(new File("src/main/java/org/activityinfo/client/page/report/ReportTitleWidget.ui.xml"));
		
		Assert.assertTrue(finder.getReferences().contains("changeTitle"));
	}
}
