package org.activityinfo.server.endpoint.kml;

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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activityinfo.server.util.xml.SimpleXmlElement;
import org.activityinfo.server.util.xml.XmlBuilder;
import org.activityinfo.server.util.xml.XmlElement;
import org.xml.sax.SAXException;

public class KMLNamespace {

    public static final String URI = "http://earth.google.com/kml/2.2";

    private final XmlBuilder builder;
    private final SimpleDateFormat xmlDateFormat;

    public KMLNamespace(XmlBuilder builder) {
        this.builder = builder;

        xmlDateFormat = (SimpleDateFormat) SimpleDateFormat.getInstance();
        xmlDateFormat.applyPattern("yyyy-MM-dd");
    }

    private XmlElement e(String name) throws SAXException {
        return builder.start(new XmlElement(URI, name));
    }

    private KMLNamespace s(String name, String value) throws SAXException {
        builder.e(new SimpleXmlElement(URI, name, value));
        return this;
    }

    public XmlElement startKml() throws SAXException {
        return e("kml");
    }

    public XmlElement startFolder() throws SAXException {
        return e("Folder");
    }

    public KMLNamespace name(String value) throws SAXException {
        return s("name", value);
    }

    public KMLNamespace open(boolean value) throws SAXException {
        return s("open", value ? "1" : "0");
    }

    public XmlElement startDocument() throws SAXException {
        return e("Document");

    }

    public XmlElement startStyle() throws SAXException {
        return e("Style");
    }

    public XmlElement startBalloonStyle() throws SAXException {
        return e("BalloonStyle");
    }

    public KMLNamespace text(String value) throws SAXException {
        return s("text", value);
    }

    public XmlElement startPlaceMark() throws SAXException {
        return e("Placemark");
    }

    public XmlElement startSnippet() throws SAXException {
        return e("Snippet");
    }

    public XmlElement startDescription() throws SAXException {
        return e("description");
    }

    public KMLNamespace styleUrl(String url) throws SAXException {
        return s("styleUrl", url);
    }

    public XmlElement startPoint() throws SAXException {
        return e("Point");
    }

    public KMLNamespace coordinates(double lng, double lat) throws SAXException {
        return s("coordinates",
            Double.toString(lng) + "," + Double.toString(lat));
    }

    public XmlElement startNetworkLink() throws SAXException {
        return e("NetworkLink");
    }

    public KMLNamespace visibility(boolean value) throws SAXException {
        return s("visibility", value ? "1" : "0");
    }

    public KMLNamespace refreshVisibility(boolean value) throws SAXException {
        return s("refreshVisibility", value ? "1" : "0");
    }

    public KMLNamespace flyToView(boolean value) throws SAXException {
        return s("flyToView", value ? "1" : "0");
    }

    public XmlElement startLink() throws SAXException {
        return e("Link");
    }

    public KMLNamespace href(String url) throws SAXException {
        return s("href", url);
    }

    public XmlElement startTimeSpan() throws SAXException {
        return e("TimeSpan");
    }

    public KMLNamespace begin(String value) throws SAXException {
        return s("begin", value);
    }

    public KMLNamespace begin(Date value) throws SAXException {
        return s("begin", xmlDateFormat.format(value));
    }

    public KMLNamespace end(Date value) throws SAXException {
        return s("end", xmlDateFormat.format(value));
    }
}
