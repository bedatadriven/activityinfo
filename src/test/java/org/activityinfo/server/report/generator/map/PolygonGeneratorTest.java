package org.activityinfo.server.report.generator.map;

import java.io.FileOutputStream;
import java.io.IOException;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.geo.TestingGeometryProvider;
import org.activityinfo.server.report.generator.MapGenerator;
import org.activityinfo.server.report.renderer.itext.PdfReportRenderer;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class})
@OnDataSet("/dbunit/polygons.db.xml")
public class PolygonGeneratorTest extends CommandTestCase2 {

	@Inject
	private MapGenerator generator;

	
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
		PdfReportRenderer pdfr = new PdfReportRenderer(new TestingGeometryProvider(), "");
		pdfr.render(map, fos);
		fos.close();
		
		

	}
	
}
