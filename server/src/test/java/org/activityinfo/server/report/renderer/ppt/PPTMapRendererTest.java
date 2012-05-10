package org.activityinfo.server.report.renderer.ppt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.report.renderer.ppt.PPTMapRenderer;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.model.MapReportElement;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.dom.client.MapElement;

public class PPTMapRendererTest {

	private MapReportElement element;

	@Before
	public void setUpDirs() {
		new File("target/report-tests").mkdirs();

		MapContent content = new MapContent();
		content.setBaseMap(GoogleBaseMap.ROADMAP);
		content.setCenter(new AiLatLng(-0.07965085325106624	, 29.326629638671875));
		content.setZoomLevel(10);
		content.setIndicators(Collections.EMPTY_SET);
		content.setMarkers(Collections.EMPTY_LIST);
		
		element = new MapReportElement();
		element.setContent(content);
		element.setWidth(1022);
		element.setHeight(634);
	}
	
	@Test
	public void renderPPtScaled() throws IOException {
			
		FileOutputStream fos = new FileOutputStream("target/report-tests/map-custom-extents.ppt");
		
		PPTMapRenderer renderer = new PPTMapRenderer("");
		renderer.render(element, fos);
		
		fos.close();
		
	}
	
	@Test
	public void renderImage() throws IOException {
		ImageMapRenderer renderer = new ImageMapRenderer("");
		renderer.renderToFile(element, new File("target/report-tests/map-custom-extents.png"));
	}
	
}
