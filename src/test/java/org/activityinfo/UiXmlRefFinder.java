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
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class UiXmlRefFinder {

    private static final String GWT_NS = "urn:ui:com.google.gwt.uibinder";
    private Class i18nClass;
    private Set<String> references = Sets.newHashSet();

    public UiXmlRefFinder(Class i18nClass) throws Exception {
        this.i18nClass = i18nClass;
    }

    public Set<String> scan(File root) throws Exception {
        for (File file : root.listFiles()) {
            if (file.getName().endsWith(".ui.xml")) {
                parseXmlFile(file);
            } else if (file.isDirectory()) {
                scan(file);
            }
        }
        return references;
    }

    public Set<String> getReferences() {
        return references;
    }

    public void parseXmlFile(File xmlFile)
        throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();

        saxParser.parse(xmlFile, new Handler());
    }

    private class Handler extends DefaultHandler {

        private Map<String, String> fieldClasses = Maps.newHashMap();

        @Override
        public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
            if (uri.equals(GWT_NS) && localName.equals("with")) {
                String field = atts.getValue("field");
                String type = atts.getValue("type");
                fieldClasses.put(field, type);
            } else {
                for (int i = 0; i != atts.getLength(); ++i) {
                    parseAttributeValue(atts.getValue(i));
                }
            }
        }

        private void parseAttributeValue(String value) {
            StringBuilder ref = new StringBuilder();
            boolean inRef = false;
            for (int i = 0; i != value.length(); ++i) {
                char c = value.charAt(i);
                if (c == '{') {
                    inRef = true;
                } else if (inRef && c == '}') {
                    parseRef(ref.toString());
                    ref.setLength(0);
                } else if (inRef) {
                    ref.append(c);
                }
            }
        }

        private void parseRef(String ref) {
            String names[] = ref.split("\\.");
            if (names.length == 2) {
                String field = names[0];
                String fieldType = fieldClasses.get(field);
                if (i18nClass.getName().equals(fieldType)) {
                    references.add(names[1]);
                }
            }
        }
    }

}
