package org.activityinfo.server.report.generator.map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.report.generator.MapGenerator;
import org.activityinfo.server.report.renderer.itext.PdfReportRenderer;
import org.activityinfo.server.report.renderer.itext.TestGeometry;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.report.content.AdminMarker;
import org.activityinfo.shared.report.content.AdminOverlay;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class})
@OnDataSet("/dbunit/polygons.db.xml")
public class PolygonGeneratorTest extends CommandTestCase2 {

	@Inject
	private MapGenerator generator;


	@Before
	public void setUpDirs() {
		new File("target/report-tests").mkdirs();
	}
	
	
	@Test
	public void basicTest() throws IOException {
		
		PolygonMapLayer layer = new PolygonMapLayer();
		layer.addIndicatorId(1);
		layer.setAdminLevelId(1383);
		
		MapReportElement map = new MapReportElement();
		map.addLayer(layer);

		MapContent content = execute(new GenerateElement<MapContent>(map));
		map.setContent(content);
		
		FileOutputStream fos = new FileOutputStream("target/report-tests/polygon.pdf");
		PdfReportRenderer pdfr = new PdfReportRenderer(TestGeometry.get(), "");
		pdfr.render(map, fos);
		fos.close();
	}
	

	@Test
	public void polygonWithHole() throws IOException {

		AdminMarker marker = new AdminMarker();
		marker.setAdminEntityId(1930);
		marker.setColor("#FFBBBB");
	
		AdminOverlay overlay = new AdminOverlay(1383);
		overlay.setOutlineColor("#FF0000");
		overlay.addPolygon(marker);
		
		MapContent content = new MapContent();
		content.setZoomLevel(8);
		content.setBaseMap(GoogleBaseMap.ROADMAP);
		content.setCenter(new AiLatLng(12.60500192642215, -7.98924994468689));
		content.getAdminOverlays().add(overlay);
		content.setFilterDescriptions(new ArrayList<FilterDescription>());
		
		PolygonMapLayer layer = new PolygonMapLayer();
		layer.addIndicatorId(1);
		layer.setAdminLevelId(1383);
		
		MapReportElement map = new MapReportElement();
		map.addLayer(layer);
		map.setContent(content);
		
		FileOutputStream fos = new FileOutputStream("target/report-tests/polygon-hole.pdf");
		PdfReportRenderer pdfr = new PdfReportRenderer(TestGeometry.get(), "");
		pdfr.render(map, fos);
		fos.close();
	}
}
