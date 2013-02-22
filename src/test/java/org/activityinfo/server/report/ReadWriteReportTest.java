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

import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer.Slice;
import org.junit.Test;

public class ReadWriteReportTest {
	
	@Test
	public void readWriteReportTest() throws Throwable	{
		Report report = new Report();
		MapReportElement map = new MapReportElement();
		map.getLayers().add(new BubbleMapLayer());
		PiechartMapLayer pielayer = new PiechartMapLayer();
		Slice slice1 = new Slice();
		slice1.setColor("FF00AA");
		slice1.setIndicatorId(1);
		Slice slice2 = new Slice();
		slice2.setColor("00FFAA");
		slice2.setIndicatorId(2);
		
		pielayer.getSlices().add(slice1);
		pielayer.getSlices().add(slice2);
		map.getLayers().add(pielayer);
		
		report.getElements().add(map);
		
		Package p = Report.class.getPackage();
				
        JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage().getName());
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        FileOutputStream fo = new FileOutputStream("SomeXmlTest.xml");
        marshaller.marshal(report, fo);
	}
}
