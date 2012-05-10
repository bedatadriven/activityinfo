package org.activityinfo.server.report.generator.map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.activityinfo.server.report.generator.map.PiechartLayerGenerator;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.model.clustering.NoClustering;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.junit.Test;

public class PiechartMapLayerGeneratorTest {

	@Test
	public void testSomething() {
		SiteDTO siteData = new SiteDTO();
		siteData.setId(42);
		siteData.setX(15.0);
		siteData.setY(0.0);
		siteData.setIndicatorValue(1, 50.0);
		siteData.setIndicatorValue(2, 10.0);
		siteData.setIndicatorValue(3, 20.0);
		siteData.setIndicatorValue(4, 40.0);
		
		PiechartMapLayer pcml = new PiechartMapLayer();
		pcml.setMinRadius(10);
		pcml.setMaxRadius(50);
		pcml.addIndicatorId(1);
		pcml.addIndicatorId(2);
		pcml.addIndicatorId(3);
		pcml.addIndicatorId(4);
		pcml.setClustering(new NoClustering());
		
		TiledMap map = new TiledMap(500, 600, new AiLatLng(15.0, 0.0), 6);

		PiechartLayerGenerator gen = new PiechartLayerGenerator(pcml, 
				Arrays.asList(siteData));
		
		MapContent mc = new MapContent(); 
		
		gen.generate(map, mc);
		
		assertThat(mc.getMarkers().size(), equalTo(1));
		assertThat(((PieMapMarker)mc.getMarkers().get(0)).getSlices().size(), equalTo(4));
	}
	
}
