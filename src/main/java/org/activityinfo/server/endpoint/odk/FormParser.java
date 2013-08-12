package org.activityinfo.server.endpoint.odk;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class FormParser {

    public SiteFormData parse(String xml) throws Exception {
        SiteFormData data = new SiteFormData();

        Element root = getRoot(xml);
        NodeList list = root.getElementsByTagName("*");
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            String name = element.getTagName();
            if ("instanceID".equals(name)) {
                data.setInstanceID(element.getTextContent());
            } else if ("activity".equals(name)) {
                data.setActivity(Integer.parseInt(element.getTextContent()));
            } else if ("partner".equals(name)) {
                data.setPartner(Integer.parseInt(element.getTextContent()));
            } else if ("locationname".equals(name)) {
                data.setLocationname(element.getTextContent());
            } else if ("gps".equals(name)) {
                data.setGps(element.getTextContent());
            } else if ("date1".equals(name)) {
                data.setDate1(date(element.getTextContent()));
            } else if ("date2".equals(name)) {
                data.setDate2(date(element.getTextContent()));
            } else if ("comments".equals(name)) {
                data.setComments(element.getTextContent());
            } else if (name.startsWith("indicator-")) {
                data.addIndicator(Integer.parseInt(name.substring(10)), element.getTextContent());
            } else if (name.startsWith("attributeGroup-")) {
                data.addAttributeGroup(Integer.parseInt(name.substring(15)), element.getTextContent());
            }
        }

        return data;
    }

    private Element getRoot(String xml) throws Exception {
        InputSource source = new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8")));
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Element root = builder.parse(source).getDocumentElement();
        if (!"data".equals(root.getTagName())) {
            XPath xPath = XPathFactory.newInstance().newXPath();
            root = (Element) xPath.evaluate("/html/head/model/instance/data", root, XPathConstants.NODE);
        }
        return root;
    }

    private Date date(String date) throws ParseException {
        if (date == null || date.isEmpty()) {
            return new Date();
        }
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }
}
