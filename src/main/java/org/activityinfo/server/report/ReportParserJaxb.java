package org.activityinfo.server.report;

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

    private static final Logger LOGGER = Logger
        .getLogger(ReportParserJaxb.class.getName());


    private static class ContextHolder {
        private final JAXBContext context;

        public ContextHolder() {
            try {
                LOGGER.info("Initializing JAXB context...");
                context = JAXBContext.newInstance(Report.class.getPackage()
                    .getName());
                LOGGER.info("JAXB Initialization complete");
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }

        public JAXBContext get() {
            return context;
        }
    }

    private static final ContextHolder CONTEXT = new ContextHolder();

    public static Report parseXml(String xml) throws JAXBException {
        return parseXML(new StringReader(xml));
    }

    public static Report parseXML(Reader reader) throws JAXBException {

        Unmarshaller um = CONTEXT.get().createUnmarshaller();
        um.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
        return (Report) um.unmarshal(reader);
    }

    public static String createXML(Report report) throws JAXBException {

        Marshaller mr = CONTEXT.get().createMarshaller();
        mr.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());

        StringWriter xml = new StringWriter();
        mr.marshal(report, xml);
        return xml.toString();
    }
}
