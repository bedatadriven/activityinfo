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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;

import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.i18n.UIMessages;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gwt.i18n.client.Messages.DefaultMessage;

public class TranslationTool {

    public static void main(String[] args) throws Exception {
        cleanConstants();
        cleanMessages();
    }

    private static void cleanConstants() throws Exception {
        Set<String> references = findReferences(UIConstants.class);
        cleanProperties(UIConstants.class, null, references);
        cleanProperties(UIConstants.class, "fr", references);
    }

    private static void cleanMessages() throws Exception {
        Set<String> references = findReferences(UIMessages.class);
        cleanProperties(loadTranslationsFromInterface(UIMessages.class),
            references);
        cleanProperties(UIMessages.class, "fr", references);
    }

    private static Set<String> findReferences(Class i18nClass) throws Exception {
        Set<String> references = Sets.newHashSet();

        File classRoot = new File("war/WEB-INF/classes");
        references.addAll(new ClassRefFinder(i18nClass).scan(classRoot));

        File srcRoot = new File("src/main/java");
        references.addAll(new UiXmlRefFinder(i18nClass).scan(srcRoot));
        return references;
    }

    private static void cleanProperties(Properties defined,
        Set<String> referenced) {
        for (Object definedKey : defined.keySet()) {
            if (!referenced.contains(definedKey)) {
                System.out.println("Remove unsused key '" + definedKey
                    + "' from UIMessages.java");
            }
        }
    }

    private static void cleanProperties(Class i18nClass, String locale,
        Set<String> references) throws IOException {
        String path = "src/main/java" + translationPath(i18nClass, locale);
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringWriter writer = new StringWriter();

        String line;
        int removed = 0;
        while ((line = reader.readLine()) != null) {
            String key = parseKey(line);
            if (key == null || references.contains(key)) {
                writer.write(line);
                writer.write("\n");
            } else {
                removed++;
            }
        }
        reader.close();

        Files.write(writer.toString(), file, Charsets.UTF_8);

        System.out.println("Removed " + removed + " unused properties from "
            + file.getName());
    }

    private static String parseKey(String line) {
        if (line.trim().isEmpty()) {
            return null;
        }
        if (line.startsWith("#")) {
            return null;
        }
        int equalPos = line.indexOf('=');
        if (equalPos == -1) {
            return null;
        }
        String key = line.substring(0, equalPos).trim();
        return key;
    }

    public static Properties loadTranslations(Class clazz, String locale)
        throws IOException {
        StringBuilder propertiesPath = translationPath(clazz, locale);
        InputStream in = clazz.getResourceAsStream(propertiesPath.toString());
        if (in == null) {
            throw new IOException("Could not find resource '"
                + propertiesPath.toString() + "'");
        }
        InputStreamReader reader = new InputStreamReader(in);
        Properties properties = new Properties();
        properties.load(reader);
        reader.close();
        return properties;
    }

    public static Properties loadTranslationsFromInterface(Class clazz) {
        Properties properties = new Properties();
        for (Method method : clazz.getMethods()) {
            DefaultMessage defaultMessage = method
                .getAnnotation(DefaultMessage.class);
            if (defaultMessage != null) {
                properties
                    .setProperty(method.getName(), defaultMessage.value());
            }
        }
        return properties;
    }

    private static StringBuilder translationPath(Class clazz, String locale) {
        StringBuilder propertiesPath = new StringBuilder();
        propertiesPath.append('/').append(clazz.getName().replace('.', '/'));
        if (locale != null) {
            propertiesPath.append("_").append(locale);
        }
        propertiesPath.append(".properties");
        return propertiesPath;
    }
}
