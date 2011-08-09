package org.sigmah.server.report.generator.map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Test;
import org.sigmah.shared.dao.SiteTableColumn;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.PieMapMarker;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.SiteData;
import org.sigmah.shared.report.model.clustering.NoClustering;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

public class PiechartMapLayerGeneratorTest {

	@Test
	public void testSomething() {
		SiteData siteData = new SiteData();
		siteData.setValue(SiteTableColumn.id, 42);
		siteData.setValue(SiteTableColumn.x, 15.0);
		siteData.setValue(SiteTableColumn.y, 0.0);
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

		PiechartLayerGenerator gen = new PiechartLayerGenerator(new MapReportElement(), pcml);
		
		MapContent mc = new MapContent(); 
		
		gen.generate(Collections.singletonList(siteData), map, mc);
		
		assertThat(mc.getMarkers().size(), equalTo(1));
		assertThat(((PieMapMarker)mc.getMarkers().get(0)).getSlices().size(), equalTo(4));
	}
	
}
