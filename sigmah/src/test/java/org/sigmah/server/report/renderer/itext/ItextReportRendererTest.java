package org.sigmah.server.report.renderer.itext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.sigmah.server.report.DummyPivotTableData;
import org.sigmah.server.report.renderer.html.ImageStorage;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.map.GoogleBaseMap;
import org.sigmah.shared.map.TileBaseMap;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.LatLng;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.content.NullContent;
import org.sigmah.shared.report.content.PivotChartContent;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.content.Point;
import org.sigmah.shared.report.content.ReportContent;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement.Type;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.TextReportElement;
import org.sigmah.shared.util.mapping.Extents;
import org.sigmah.shared.util.mapping.TileMath;

public class ItextReportRendererTest {
	
	@Test
	public void pageNumbers() throws IOException {
		
		ReportContent content = new ReportContent();
		content.setFilterDescriptions(Collections.EMPTY_LIST);
		
		Report report = new Report();
		report.setContent(content);
		
		for(int i=0;i!=1000;++i) {
			TextReportElement element = new TextReportElement();
			element.setText("Quick brown fox, texte français");
			element.setContent(new NullContent());
			
			report.addElement(element);
		}
		
		renderToPdf(report, "pagenumbers.pdf");	
	}

	
	@Test
	public void htmlImages() throws IOException {
		
		DummyPivotTableData data = new DummyPivotTableData();
				
		PivotChartContent chartContent = new PivotChartContent();
		chartContent.setData(data.table);
		chartContent.setYMin(0);
		chartContent.setYStep(100);
		chartContent.setFilterDescriptions(Collections.EMPTY_LIST);
		
		PivotChartReportElement chart = new PivotChartReportElement(Type.Pie);
		chart.setTitle("My Pie Chart");
		chart.setCategoryDimensions(data.colDims);
		chart.setSeriesDimension(data.rowDims);
		chart.setContent(chartContent);
	
		PivotContent tableContent = new PivotContent();
		tableContent.setFilterDescriptions(Collections.EMPTY_LIST);
		tableContent.setData(data.table);
		
		PivotTableReportElement table = new PivotTableReportElement();
		table.setColumnDimensions(data.colDims);
		table.setRowDimensions(data.rowDims);
		table.setTitle("My Table");
		table.setContent(tableContent);
		
		BubbleMapMarker marker1 = new BubbleMapMarker();
		marker1.setLat(-2.45);
		marker1.setLng(28.8);
		marker1.setX(100);
		marker1.setY(100);
		marker1.setRadius(25);
		
		TileBaseMap baseMap = new TileBaseMap();
		baseMap.setTileUrlPattern("http://mt{s}.aimaps.net/nordkivu.cd/v1/z{z}/{x}x{y}.png");
		
		MapContent mapContent = new MapContent();
		mapContent.setFilterDescriptions(Collections.EMPTY_LIST);
		mapContent.setBaseMap(baseMap);
		mapContent.setZoomLevel(8);
		mapContent.setExtents(new Extents(-2.2, -2.1, 28.85, 28.9));
		mapContent.setMarkers(Arrays.asList((MapMarker)marker1));
		
		MapReportElement map = new MapReportElement();
		map.setTitle("My Map");
		map.setContent(mapContent);
		
		ReportContent content = new ReportContent();
		content.setFilterDescriptions(Collections.EMPTY_LIST);
		
		Report report = new Report();
		report.setContent(content);
		report.addElement(chart);
		report.addElement(table);
		report.addElement(new TextReportElement("Testing 1..2.3.. français"));
		report.addElement(map);
		
		renderToPdf(report, "piechart.pdf");
		renderToHtml(report, "piechart.html");
		renderToHtmlUsingWriter(report, "piechart2.html");
		renderToRtf(report, "piechart.rtf");
	}
	
	@Test
	public void googleMapsBaseMap() throws IOException {
	
		ReportContent content = new ReportContent();
		content.setFilterDescriptions(Collections.EMPTY_LIST);
		
		Report report = new Report();
		report.setContent(content);
	
		
		TileBaseMap referenceBaseMap = new TileBaseMap();
		referenceBaseMap.setTileUrlPattern("http://mt{s}.aimaps.net/admin/v1/z{z}/{x}x{y}.png");
		referenceBaseMap.setName("Administrative Map");
		
		BaseMap[] baseMaps = new BaseMap[] { 
				referenceBaseMap,
				GoogleBaseMap.HYBRID,
				GoogleBaseMap.ROADMAP,
				GoogleBaseMap.SATELLITE,
				GoogleBaseMap.TERRAIN
		};
		
		for(BaseMap baseMap : baseMaps) {
		
			BubbleMapMarker marker1 = new BubbleMapMarker();
			marker1.setLat(-2.45);
			marker1.setLng(28.8);
			marker1.setX(100);
			marker1.setY(100);
			marker1.setRadius(25);
			
			MapContent mapContent = new MapContent();
			mapContent.setFilterDescriptions(Collections.EMPTY_LIST);
			mapContent.setBaseMap(baseMap);
			mapContent.setZoomLevel(8);
			mapContent.setExtents(new Extents(-2.2, -2.1, 28.85, 28.9));
			mapContent.setMarkers(Arrays.asList((MapMarker)marker1));
			
			MapReportElement satelliteMap = new MapReportElement();
			satelliteMap.setTitle(baseMap.toString());
			satelliteMap.setContent(mapContent);
			report.addElement(satelliteMap);
		}
		

		
		renderToPdf(report, "google map.pdf");
	}


	private void renderTo(Report report, ItextReportRenderer reportRenderer, String name)
			throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream("target/report-tests/" + name);
		reportRenderer.render(report, fos);		
		fos.close();
	}
	
	private void renderToPdf(Report report, String name) throws IOException {
		PdfReportRenderer reportRenderer = new PdfReportRenderer("");
		renderTo(report, reportRenderer, name);
	}
	
	private void renderToHtml(Report report, String name) throws IOException {
		renderTo(report, new HtmlReportRenderer("", new TestImageStorageProvider()), name);
	}
	
	private void renderToHtmlUsingWriter(Report report, String name) throws IOException {
		FileWriter writer = new FileWriter("target/report-tests/" + name);
		HtmlReportRenderer renderer = new HtmlReportRenderer("", new TestImageStorageProvider());
		renderer.render(report, writer);
		writer.close();
		
	}
	
	private void renderToRtf(Report report, String name) throws IOException {
		renderTo(report, new RtfReportRenderer(""), name);
	}
	
	private static class TestImageStorageProvider implements ImageStorageProvider {

		private int nextId = 1;
		
		@Override
		public ImageStorage getImageUrl(String suffix) throws IOException {			
			String fileName = (nextId++) + suffix;
			
			return new ImageStorage(fileName, new FileOutputStream("target/report-tests/" + fileName));
		}
	}
}
