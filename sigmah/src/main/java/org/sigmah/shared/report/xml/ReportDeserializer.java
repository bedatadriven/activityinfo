package org.sigmah.shared.report.xml;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;
/*
 * @author Alex Bertram
 */

public class ReportDeserializer {

    public ReportElement deserialize(Document document) throws DeserializatonException {
        return deserialize(document.getDocumentElement());
    }

    public ReportElement deserialize(Element element) throws DeserializatonException {
        if("report".equals(element.getNodeName())) {
            return deserializeReport(element);
        } else if("pivotTable".equals(element.getNodeName())) {
            return deserializePivotTable(element);
        } else if("pivotChart".equals(element.getNodeName())) {
            return deserializePivotChart(element);
        } else if("table".equals(element.getNodeName())) {
            return deserializeTable(element);
        } else if("map".equals(element.getNodeName())) {
            return deserializeMap(element);
        } else {
            throw new DeserializatonException("Unknown element " + element.getNodeName());
        }
    }

    private ReportElement deserializePivotTable(Element element) {
        return null;
    }

    private ReportElement deserializePivotChart(Element element) {
        return null;
    }

    private ReportElement deserializeMap(Element element) {
        return null;
    }

    private ReportElement deserializeTable(Element element) {
        return null;
    }

    private ReportElement deserializeReport(Element element) {

        Report report = new Report();
        readElementProperties(report, element);

        return report;

    }

    private void readElementProperties(ReportElement reportElement, Element xmlElement) {
        reportElement.setTitle(getTextElement(xmlElement, "title"));
        reportElement.setSheetTitle(getTextElement(xmlElement, "sheetTitle"));
    }

    private String getTextElement(Element parent, String name) {
        NodeList list = parent.getElementsByTagName(name);
        if(list.getLength() == 0) {
            return null;
        } else {
            NodeList children = parent.getChildNodes();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i!=children.getLength(); ++i) {
                if(children.item(i) instanceof Text) {
                    sb.append(children.item(i).toString());
                }
            }
            return sb.toString();
        }
    }
}
