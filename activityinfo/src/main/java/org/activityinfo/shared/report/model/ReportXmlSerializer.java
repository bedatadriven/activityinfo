package org.activityinfo.shared.report.model;
/*
 * @author Alex Bertram
 */

public class ReportXmlSerializer {

    public void serialize(ReportElement element, StringBuilder xml) {
        if(element instanceof MapElement) {
            serializeMapElement((MapElement) element, xml);
        }
    }

    private void serializeMapElement(MapElement element, StringBuilder xml) {
        
    }

}
