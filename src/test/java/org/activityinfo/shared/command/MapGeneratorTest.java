package org.activityinfo.shared.command;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.report.ReportModule;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class, ReportModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class MapGeneratorTest extends CommandTestCase2 {


	@Test
	public void adminMapIcon() {
		
		AdministrativeLevelClustering clustering = new AdministrativeLevelClustering();
		clustering.getAdminLevels().add(1);
		
		IconMapLayer layer = new IconMapLayer();
		layer.setClustering(clustering);
		layer.getIndicatorIds().add(1);
		
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Site, 3);
		
		MapReportElement map = new MapReportElement();
		map.addLayer(layer);
		map.setFilter(filter);
			
		MapContent result = (MapContent)execute(new GenerateElement(map));

		System.out.println(result.getMarkers());

		IconMapMarker marker = (IconMapMarker) result.getMarkers().get(0);
		assertThat(marker.getSiteIds().size(), equalTo(1));
		assertThat(marker.getSiteIds().get(0), equalTo(3));

		System.out.println(marker.getTitle());
		
		//assertThat(marker., equalTo(10000d));		
		assertThat(result.getUnmappedSites().size(), equalTo(0));	
	}
	
	@Test
	public void adminMapBubbles() {
		
		AdministrativeLevelClustering clustering = new AdministrativeLevelClustering();
		clustering.getAdminLevels().add(1);
		
		BubbleMapLayer layer = new BubbleMapLayer();
		layer.setClustering(clustering);
		layer.addIndicator(1);
		
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Site, 3);
		
		MapReportElement map = new MapReportElement();
		map.addLayer(layer);
		map.setFilter(filter);
		
		MapContent result = (MapContent)execute(new GenerateElement(map));

		System.out.println(result.getMarkers());

		BubbleMapMarker marker = (BubbleMapMarker) result.getMarkers().get(0);
		assertThat(marker.getSiteIds().size(), equalTo(1));
		assertThat(marker.getSiteIds().get(0), equalTo(3));

		assertThat(marker.getValue(), equalTo(10000d));
		
		assertThat(result.getUnmappedSites().size(), equalTo(0));
		
		System.out.println(marker.getTitle());
		
	}
}
