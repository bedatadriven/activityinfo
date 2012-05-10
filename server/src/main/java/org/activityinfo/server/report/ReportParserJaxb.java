/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;

import org.activityinfo.shared.report.model.Report;

/**
 * @author Alex Bertram
 */
public class ReportParserJaxb {

    public static Report parseXml(String xml) throws JAXBException {
        return parseXML(new StringReader(xml));
    }

    public static Report parseXML(Reader reader) throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage().getName());
        Unmarshaller um = jc.createUnmarshaller();
        um.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
        return (Report) um.unmarshal(reader);
    }
    
    public static String createXML(Report report) throws JAXBException{
    	
    	JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage().getName());
    	Marshaller mr = jc.createMarshaller();
    	mr.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
    	
    	StringWriter xml = new StringWriter();
		mr.marshal(report, xml);
		return xml.toString();
    }
}
