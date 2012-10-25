package org.activityinfo.server.report.renderer.itext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.activityinfo.server.geo.TestingGeometryProvider;
import org.activityinfo.server.report.DummyPivotTableData;
import org.activityinfo.server.report.output.TempStorage;
import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.excel.ExcelReportRenderer;
import org.activityinfo.server.report.renderer.itext.HtmlReportRenderer;
import org.activityinfo.server.report.renderer.itext.PdfReportRenderer;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.server.report.renderer.ppt.PPTMapRenderer;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.map.TileBaseMap;
import org.activityinfo.shared.report.content.BubbleLayerLegend;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.IconLayerLegend;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.content.NullContent;
import org.activityinfo.shared.report.content.PieChartLegend;
import org.activityinfo.shared.report.content.PivotChartContent;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.content.ReportContent;
import org.activityinfo.shared.report.content.SimpleCategory;
import org.activityinfo.shared.report.content.PivotTableData.Axis;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapIcon;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.TextReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement.Type;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.util.mapping.Extents;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ItextReportRendererTest {
	
	private Comparator<PivotTableData.Axis> comparator = new Comparator<PivotTableData.Axis>() {
		
		@Override
		public int compare(Axis arg0, Axis arg1) {
			return arg0.getLabel().compareTo(arg1.getLabel());
		}
	};

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
		mapContent.setCenter(new Extents(-2.2, -2.1, 28.85, 28.9).center());
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
		//renderToHtmlUsingWriter(report, "piechart2.html");
		renderToRtf(report, "piechart.rtf");
	}
	
	@Test
	public void chartTest() throws IOException {
		
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
		

		ReportContent content = new ReportContent();
		content.setFilterDescriptions(Collections.EMPTY_LIST);
		
		Report report = new Report();
		report.setContent(content);
		report.addElement(chart);
		
		renderToPdf(report, "chartTest.pdf");	
		renderToRtf(report, "chartTest.rtf");
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
			mapContent.setCenter(new Extents(-2.2, -2.1, 28.85, 28.9).center());
			mapContent.setMarkers(Arrays.asList((MapMarker)marker1));
			
			MapReportElement satelliteMap = new MapReportElement();
			satelliteMap.setTitle(baseMap.toString());
			satelliteMap.setContent(mapContent);
			report.addElement(satelliteMap);
		}
		

		
		//renderToPdf(report, "google map.pdf");
		//renderToHtml(report, "google map.html");
		renderToRtf(report, "google map.rtf");
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
		mapContent.setCenter(new Extents(-2.2, -2.1, 28.85, 28.9).center());
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


	@Test
	public void nestedColumns() throws IOException {

		Dimension province = new AdminDimension(1);
		Dimension partner = new Dimension(DimensionType.Partner);
		Dimension year = new DateDimension(DateUnit.YEAR);
		Dimension month = new DateDimension(DateUnit.MONTH);
		
		EntityCategory avsi = new EntityCategory(100, "AVSI RRMP");
		
		PivotTableData data = new PivotTableData();
		
		Axis sudKivu = data.getRootColumn().addChild(province, new EntityCategory(1, "Sud Kivu"),
				"Sud Kivu", comparator);
		Axis y2010 = sudKivu.addChild(year, new SimpleCategory("2010"), "2010", comparator);
		Axis y2011 = sudKivu.addChild(year, new SimpleCategory("2010"), "2010", comparator);		
		Axis jan2010 = y2010.addChild(month, new SimpleCategory("Jan"), "Jan", comparator);
		Axis feb2010 = y2010.addChild(month, new SimpleCategory("Feb"), "Feb", comparator);
		Axis jan2011 = y2011.addChild(month, new SimpleCategory("Jan"), "Jan", comparator);
		Axis feb2011 = y2011.addChild(month, new SimpleCategory("Feb"), "Feb", comparator);

		Axis avsiRow = data.getRootRow().addChild(partner, avsi, avsi.getLabel(), comparator);
		avsiRow.setValue(jan2010, 1d);
		avsiRow.setValue(feb2010, 2d);
		avsiRow.setValue(jan2011, 3d);
		avsiRow.setValue(feb2011, 4d);
		
		PivotContent tableContent = new PivotContent(data, Lists.<FilterDescription>newArrayList());
		
		PivotTableReportElement table = new PivotTableReportElement();
		table.addRowDimension(partner);
		table.addColDimension(province);
		table.addColDimension(year);
		table.addColDimension(month);
		table.setContent(tableContent);
		
		ReportContent content = new ReportContent();
		content.setFilterDescriptions(Collections.EMPTY_LIST);
		
		Report report = new Report();
		report.setContent(content);
		report.addElement(table);

		renderToPdf(report, "nestedColumns.pdf");
		renderToRtf(report, "nestedColumns.rtf");		
		renderToXls(report, "nestedColumns.xls");
	}


	@Test
	public void manyColumns() throws IOException {

		
		PivotTableData data = new PivotTableData();

		Dimension partner = new Dimension(DimensionType.Partner);
		Dimension year = new DateDimension(DateUnit.YEAR);
		
		EntityCategory avsi = new EntityCategory(100, "AVSI RRMP");
		Axis avsiRow = data.getRootRow().addChild(partner, avsi, avsi.getLabel(), comparator);
		
		for(int y=2011;y<2030;++y) {
			Axis col = data.getRootColumn().addChild(year, new SimpleCategory("" + y), "" + y, comparator);
			avsiRow.setValue(col, (double)y);
		}
		
		PivotContent tableContent = new PivotContent(data, Lists.<FilterDescription>newArrayList());
		
		PivotTableReportElement table = new PivotTableReportElement();
		table.addRowDimension(partner);
		table.addColDimension(year);
		table.setContent(tableContent);
		
		ReportContent content = new ReportContent();
		content.setFilterDescriptions(Collections.EMPTY_LIST);
		
		Report report = new Report();
		report.setContent(content);
		report.addElement(table);

		renderToPdf(report, "narrowColumns.pdf");
		renderToRtf(report, "narrowColumns.rtf");		
	}

	
	private String mapIconPath() {
		return "war/mapicons";
	}

	private void renderTo(Report report, Renderer reportRenderer, String name)
			throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream("target/report-tests/" + name);
		reportRenderer.render(report, fos);		
		fos.close();
	}
	
	private void renderToPdf(Report report, String name) throws IOException {
		PdfReportRenderer reportRenderer = new PdfReportRenderer(new TestingGeometryProvider(), mapIconPath());
		renderTo(report, reportRenderer, name);
	}
	

	private void renderToXls(Report report, String name) throws IOException {
		ExcelReportRenderer reportRenderer = new ExcelReportRenderer();
		renderTo(report, reportRenderer, name);
	}
	
	private void renderToHtml(Report report, String name) throws IOException {
		renderTo(report, new HtmlReportRenderer(new TestingGeometryProvider(),  mapIconPath(), new TestImageStorageProvider()), name);
	}
	
	private void renderToHtmlUsingWriter(Report report, String name) throws IOException {
		FileWriter writer = new FileWriter("target/report-tests/" + name);
		HtmlReportRenderer renderer = new HtmlReportRenderer(new TestingGeometryProvider(), mapIconPath(), new TestImageStorageProvider());
		renderer.render(report, writer);
		writer.close();
	}
	
	private void renderToRtf(Report report, String name) throws IOException {
		renderTo(report, new RtfReportRenderer(new TestingGeometryProvider(), mapIconPath()), name);
	}
	
	private void renderToPpt(MapReportElement map, String name) throws FileNotFoundException, IOException {
		PPTMapRenderer renderer = new PPTMapRenderer(new TestingGeometryProvider(), mapIconPath());
		renderer.render(map, new FileOutputStream("target/report-tests/" + name));
	}
	
	private static class TestImageStorageProvider implements StorageProvider {

		private static int nextId = 1;
		
		@Override
		public TempStorage allocateTemporaryFile(String mimeType, String suffix) throws IOException {			
			String fileName = (nextId++) + suffix;
			File file = new File("target/report-tests/" + fileName);
			return new TempStorage(file.toURI().toURL().toString(), new FileOutputStream(file));
		}
	}
}
