package org.sigmah.server.report;

import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.junit.Test;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer.Slice;

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
