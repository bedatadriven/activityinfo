package org.sigmah.server.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.report.renderer.excel.ExcelChartRenderer;
import org.sigmah.server.report.renderer.excel.ExcelPivotTableRenderer;
import org.sigmah.server.report.renderer.excel.ExcelReportRenderer;
import org.sigmah.server.report.renderer.excel.ExcelStaticRenderer;
import org.sigmah.server.report.renderer.excel.ExcelTableRenderer;
import org.sigmah.server.report.renderer.itext.HtmlReportRenderer;
import org.sigmah.server.report.renderer.itext.ItextChartRenderer;
import org.sigmah.server.report.renderer.itext.ItextMapRenderer;
import org.sigmah.server.report.renderer.itext.ItextPivotTableRenderer;
import org.sigmah.server.report.renderer.itext.ItextStaticRenderer;
import org.sigmah.server.report.renderer.itext.ItextTableRenderer;
import org.sigmah.server.report.renderer.itext.PdfReportRenderer;
import org.sigmah.server.report.renderer.itext.RtfReportRenderer;
import org.sigmah.server.report.util.HtmlWriter;
import org.sigmah.shared.report.content.FilterDescription;
import org.sigmah.shared.report.content.ReportContent;
import org.sigmah.shared.report.model.Report;


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
		PdfReportRenderer renderer = new PdfReportRenderer(new ItextPivotTableRenderer(),
                new ItextChartRenderer(null), new ItextMapRenderer(null), new ItextTableRenderer(null), new ItextStaticRenderer()); 
	
		FileOutputStream fos = new FileOutputStream("target/report-tests/render-static" + renderer.getFileSuffix());
		renderer.render(r, fos);
		fos.close();
	}
	
	@Test
	public void testRtfRender() throws JAXBException, IOException {
		Report r = getStatic();
		RtfReportRenderer renderer = new RtfReportRenderer(new ItextPivotTableRenderer(),
                new ItextChartRenderer(null), new ItextMapRenderer(null), new ItextTableRenderer(null), new ItextStaticRenderer()); 
	
		FileOutputStream fos = new FileOutputStream("target/report-tests/render-static" + renderer.getFileSuffix());
		renderer.render(r, fos);
		fos.close();
	}

	@Test
	public void testExcelRender() throws JAXBException, IOException {
		Report r = getStatic();
		ExcelReportRenderer renderer = new ExcelReportRenderer(new ExcelPivotTableRenderer(),
                new ExcelTableRenderer(), new ExcelChartRenderer(), new ExcelStaticRenderer()); 
	
		FileOutputStream fos = new FileOutputStream("target/report-tests/render-static" + renderer.getFileSuffix());
		renderer.render(r, fos);
		fos.close();
	}
	
	@Test
	public void testHtmlRender() throws JAXBException, IOException {
		Report r = getStatic();
		HtmlReportRenderer renderer =new HtmlReportRenderer(new ItextPivotTableRenderer(),
                new ItextChartRenderer(null), new ItextMapRenderer(null), new ItextTableRenderer(null), new ItextStaticRenderer()); 
		FileOutputStream fos = new FileOutputStream("target/report-tests/render-static" + renderer.getFileSuffix());
		renderer.render(r, fos);
		fos.close();
	}
	
	@Test
	public void testOldHtmlRender() throws JAXBException, IOException {
		Report r = getStatic();
		org.sigmah.server.report.renderer.html.HtmlReportRenderer renderer = new org.sigmah.server.report.renderer.html.HtmlReportRenderer(new org.sigmah.server.report.renderer.html.HtmlPivotTableRenderer(), 
				new org.sigmah.server.report.renderer.html.HtmlChartRendererJC(),
				new org.sigmah.server.report.renderer.html.HtmlTableRenderer(null),
				new org.sigmah.server.report.renderer.html.HtmlMapRenderer(null),
				new org.sigmah.server.report.renderer.html.HtmlStaticRenderer());

		FileWriter fw = new FileWriter("target/report-tests/render-static-old.html");
		HtmlWriter writer = new HtmlWriter();;
		renderer.render(writer, new NullImageStorageProvider(), r);
		fw.write(writer.toString());
		fw.close();
	}
	
	



}
