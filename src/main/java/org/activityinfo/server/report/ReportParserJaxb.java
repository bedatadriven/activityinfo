/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.activityinfo.shared.report.model.Report;


/**
 * @author Alex Bertram
 */
public class ReportParserJaxb {

	private static final Logger LOGGER = Logger.getLogger(ReportParserJaxb.class.getName());
	
	/**
	 * n4%icZ9YU$3j
	 */
	private static class ContextHolder {
		private final JAXBContext context;
		public ContextHolder() {
			try {
				LOGGER.info("Initializing JAXB context...");
				context = JAXBContext.newInstance(Report.class.getPackage().getName());
				LOGGER.info("JAXB Initialization complete");
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}
		public JAXBContext get() {
			return context;
		}
	}
	
	private static ContextHolder CONTEXT = new ContextHolder();
		
    public static Report parseXml(String xml) throws JAXBException {
        return parseXML(new StringReader(xml));
    }

    public static Report parseXML(Reader reader) throws JAXBException {

        Unmarshaller um = CONTEXT.get().createUnmarshaller();
        um.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
        return (Report) um.unmarshal(reader);
    }
    
    public static String createXML(Report report) throws JAXBException{
    	
    	Marshaller mr = CONTEXT.get().createMarshaller();
    	mr.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
    	
    	StringWriter xml = new StringWriter();
		mr.marshal(report, xml);
		return xml.toString();
    }
}
