package org.activityinfo.server.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.activityinfo.server.report.renderer.itext.HtmlReportRenderer;
import org.activityinfo.server.report.renderer.itext.PdfReportRenderer;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.ReportContent;
import org.activityinfo.shared.report.model.Report;
import org.junit.Before;
import org.junit.Test;


public class StaticElementRenderTest{

	 @Before
	 public void setup() {
		File file = new File("target/report-tests");
		file.mkdirs();
	 }


	public Report parseXml(String filename) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage().getName());
		Unmarshaller um = jc.createUnmarshaller();
		um.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		return (Report) um.unmarshal(new InputStreamReader(
				getClass().getResourceAsStream("/report-def/parse-test/" + filename)));
	}
	
	public Report getStatic() throws JAXBException {
		Report r = parseXml("static.xml");
		r.setContent( new ReportContent());
		r.getContent().setFilterDescriptions(new ArrayList <FilterDescription> ());
		return r;
	}

	public static void dumpXml(Report report) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage().getName());
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(report, System.out);
	}


	@Test
	public void testPdfRender() throws JAXBException, IOException {
		Report r = getStatic();
		PdfReportRenderer renderer = new PdfReportRenderer(""); 
	
		FileOutputStream fos = new FileOutputStream("target/report-tests/render-static" + renderer.getFileSuffix());
		renderer.render(r, fos);
		fos.close();
	}
	
	@Test
	public void testRtfRender() throws JAXBException, IOException {
		Report r = getStatic();
		RtfReportRenderer renderer = new RtfReportRenderer(""); 
	
		FileOutputStream fos = new FileOutputStream("target/report-tests/render-static" + renderer.getFileSuffix());
		renderer.render(r, fos);
		fos.close();
	}
	
	@Test
	public void testHtmlRender() throws JAXBException, IOException {
		Report r = getStatic();
		HtmlReportRenderer renderer =new HtmlReportRenderer("", new NullStorageProvider()); 
		FileOutputStream fos = new FileOutputStream("target/report-tests/render-static" + renderer.getFileSuffix());
		renderer.render(r, fos);
		fos.close();
	}
	

}
