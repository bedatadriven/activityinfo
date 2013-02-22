package org.activityinfo;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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

        Properties english = TranslationTool.loadTranslations(
            UIConstants.class, null);
        Properties french = TranslationTool.loadTranslations(UIConstants.class,
            "fr");

        checkForMissingTranslations("UIConstants_fr.properties", english,
            french);
    }

    @Test
    public void messages() throws IOException {
        Properties english = TranslationTool
            .loadTranslationsFromInterface(UIMessages.class);
        Properties french = TranslationTool.loadTranslations(UIMessages.class,
            "fr");

        checkForMissingTranslations("UIMessages_fr.properties", english, french);
    }

    private void checkForMissingTranslations(String file, Properties english,
        Properties french) throws AssertionError {
        boolean missingTranslations = false;

        StringBuilder msg = new StringBuilder();
        msg.append("The following translations need to be added to " + file);
        for (Object key : english.keySet()) {
            if (!french.containsKey(key)) {
                missingTranslations = true;
                msg.append("\n").append(key).append(" = ")
                    .append(english.get(key));
            }
        }

        if (missingTranslations) {
            throw new AssertionError(msg.toString());
        }
    }

    @Test
    public void testParseXml() throws Exception {
        UiXmlRefFinder finder = new UiXmlRefFinder(UIConstants.class);
        finder
            .parseXmlFile(new File(
                "src/main/java/org/activityinfo/client/page/report/ReportTitleWidget.ui.xml"));

        Assert.assertTrue(finder.getReferences().contains("changeTitle"));
    }
}
