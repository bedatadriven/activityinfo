package org.sigmah.server.report;

import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.junit.Test;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;

public class ReadWriteReportTest {
	
	@Test
	public void readWriteReportTest() throws Throwable
	{
		Report report = new Report();
		MapReportElement map = new MapReportElement();
		map.getLayers().add(new BubbleMapLayer());
		
		report.getElements().add(map);
		
        JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage().getName());
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        FileOutputStream fo = new FileOutputStream("SomeXmlTest.xml");
        marshaller.marshal(report, fo);
	}
}
