/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import org.sigmah.shared.report.model.Report;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.StringReader;

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
}
