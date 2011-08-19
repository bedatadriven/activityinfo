package org.sigmah.server.report.renderer.itext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.report.DummyPivotTableData;
import org.sigmah.server.report.renderer.html.ImageStorage;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;
import org.sigmah.server.report.renderer.ppt.PPTMapRenderer;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.map.GoogleBaseMap;
import org.sigmah.shared.map.TileBaseMap;
import org.sigmah.shared.report.content.BubbleLayerLegend;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.IconLayerLegend;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.content.NullContent;
import org.sigmah.shared.report.content.PieChartLegend;
import org.sigmah.shared.report.content.PivotChartContent;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.content.ReportContent;
import org.sigmah.shared.report.model.MapIcon;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement.Type;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.TextReportElement;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;
import org.sigmah.shared.util.mapping.Extents;

import com.google.inject.internal.Lists;

public class ItextReportRendererTest {
	
	@Before
	public void setUpDirs() {
		new File("target/report-tests").mkdirs();
	}
	
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
	
	@Test
	public void legendTest() throws IOException {

		BubbleMapMarker marker1 = new BubbleMapMarker();
		marker1.setLat(-2.45);
		marker1.setLng(28.8);
		marker1.setX(100);
		marker1.setY(100);
		marker1.setRadius(25);
		marker1.setValue(300);
		
		
		TileBaseMap baseMap = new TileBaseMap();
		baseMap.setTileUrlPattern("http://mt{s}.aimaps.net/nordkivu.cd/v1/z{z}/{x}x{y}.png");
		
		BubbleMapLayer layer1 = new BubbleMapLayer();
		layer1.addIndicatorId(101);
		layer1.setMinRadius(10);
		layer1.setMaxRadius(10);
		
		BubbleLayerLegend legend1 = new BubbleLayerLegend();
		legend1.setDefinition(layer1);
		legend1.setMinValue(1000);
		legend1.setMaxValue(3000);
		
		BubbleMapLayer layer2 = new BubbleMapLayer();
		layer2.addIndicatorId(102);
		layer2.addIndicatorId(103);
		layer2.setMinRadius(10);
		layer2.setMaxRadius(25);
		
		BubbleLayerLegend legend2 = new BubbleLayerLegend();
		legend2.setDefinition(layer2);
		legend2.setMinValue(600);
		legend2.setMaxValue(999);
		
		IconMapLayer layer3 = new IconMapLayer();
		layer3.setIcon(MapIcon.Icon.Default.name());
		layer3.getIndicatorIds().add(101);
		
		IconLayerLegend legend3 = new IconLayerLegend();
		legend3.setDefinition(layer3);
		
		List<PieChartLegend> pieChartLegends = Lists.newArrayList();
		List<PiechartMapLayer> pieChartLayers = Lists.newArrayList();
		int indicatorIds [] = new int[] { 101, 102, 103 };
		
		for(int sliceCount=1;sliceCount<10;++sliceCount) {

			PiechartMapLayer pieChartLayer = new PiechartMapLayer();
			for(int i=0;i!=sliceCount;++i) {
				pieChartLayer.addIndicatorId( indicatorIds[ i % indicatorIds.length ] );
			}
			pieChartLayer.setMinRadius(25);
			pieChartLayer.setMaxRadius(25);
			
			PieChartLegend pieChartLegend = new PieChartLegend();
			pieChartLegend.setDefinition(pieChartLayer);	
			
			pieChartLayers.add(pieChartLayer);
			pieChartLegends.add(pieChartLegend);
		}
		
			
		IndicatorDTO indicator101 = new IndicatorDTO();
		indicator101.setId(101);
		indicator101.setName("Nombre de salles de classe fonctionnelles (construites, rehabilitees, equipees) " +
				"pour l'education formelle et non formelle.");
		
		IndicatorDTO indicator102 = new IndicatorDTO();
		indicator102.setId(102);
		indicator102.setName("Nombre d'enfants ayant beneficie de kits scolaires, recreatifs et didactiques");
		
		IndicatorDTO indicator103 = new IndicatorDTO();
		indicator103.setId(103);
		indicator103.setName("Pourcentage des ménages qui utilsent la moustiquaire rationnellement");
				
		
		MapContent mapContent = new MapContent();
		mapContent.setFilterDescriptions(Collections.EMPTY_LIST);
		mapContent.setBaseMap(baseMap);
		mapContent.setZoomLevel(8);
		mapContent.setExtents(new Extents(-2.2, -2.1, 28.85, 28.9));
		mapContent.setMarkers(Arrays.asList((MapMarker)marker1));
		mapContent.getIndicators().addAll(Arrays.asList(
				indicator101, indicator102, indicator103));
		mapContent.addLegend(legend1);
		mapContent.addLegend(legend2);
		mapContent.addLegend(legend3);
		mapContent.getLegends().addAll(pieChartLegends);
		
		MapReportElement map = new MapReportElement();
		map.setTitle("My Map");
		map.setContent(mapContent);
		map.addLayer(layer1);
		map.addLayer(layer2);
		map.addLayer(layer3);
		map.getLayers().addAll(pieChartLayers);
		
		
		ReportContent content = new ReportContent();
		content.setFilterDescriptions(Collections.EMPTY_LIST);
		
		Report report = new Report();
		report.setContent(content);
		report.addElement(map);
		
		renderToPdf(report, "legend.pdf");
		renderToHtml(report, "legend.html");
		renderToRtf(report, "legend.rtf");
		renderToPpt(map, "legend.ppt");
	}



	private String mapIconPath() {
		return "war/mapicons";
	}

	private void renderTo(Report report, ItextReportRenderer reportRenderer, String name)
			throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream("target/report-tests/" + name);
		reportRenderer.render(report, fos);		
		fos.close();
	}
	
	private void renderToPdf(Report report, String name) throws IOException {
		PdfReportRenderer reportRenderer = new PdfReportRenderer(mapIconPath());
		renderTo(report, reportRenderer, name);
	}
	
	private void renderToHtml(Report report, String name) throws IOException {
		renderTo(report, new HtmlReportRenderer(mapIconPath(), new TestImageStorageProvider()), name);
	}
	
	private void renderToHtmlUsingWriter(Report report, String name) throws IOException {
		FileWriter writer = new FileWriter("target/report-tests/" + name);
		HtmlReportRenderer renderer = new HtmlReportRenderer(mapIconPath(), new TestImageStorageProvider());
		renderer.render(report, writer);
		writer.close();
	}
	
	private void renderToRtf(Report report, String name) throws IOException {
		renderTo(report, new RtfReportRenderer(mapIconPath()), name);
	}
	
	private void renderToPpt(MapReportElement map, String name) throws FileNotFoundException, IOException {
		PPTMapRenderer renderer = new PPTMapRenderer(mapIconPath());
		renderer.render(map, new FileOutputStream("target/report-tests/" + name));
	}
	
	private static class TestImageStorageProvider implements ImageStorageProvider {

		private static int nextId = 1;
		
		@Override
		public ImageStorage getImageUrl(String suffix) throws IOException {			
			String fileName = (nextId++) + suffix;
			File file = new File("target/report-tests/" + fileName);
			return new ImageStorage(file.toURI().toURL().toString(), new FileOutputStream(file));
		}
	}
}
