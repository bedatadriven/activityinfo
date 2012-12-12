package org.activityinfo.shared.command;

import java.util.Arrays;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.report.ReportModule;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class, ReportModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GeneratePivotTableHandlerTest extends CommandTestCase2 {

	
	@Test
	public void serverSide() throws CommandException {
		
		PivotTableReportElement element = new PivotTableReportElement();
		element.setRowDimensions(Arrays.asList(new Dimension(DimensionType.Indicator)));
		element.setColumnDimensions(Arrays.asList(new Dimension(DimensionType.Partner)));
		
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Indicator, Arrays.asList(1,2,103));
		element.setFilter(filter);

		PivotContent content = execute(new GeneratePivotTable(element));
		
//		TODO real test
//		System.out.println(content.getData());
		
	}
	
	@Test
	public void withNullAttribute() throws CommandException {
		PivotTableReportElement element = new PivotTableReportElement();
		element.setRowDimensions(Arrays.asList(new Dimension(DimensionType.Indicator)));
		element.setColumnDimensions(Arrays.asList((Dimension)new AttributeGroupDimension(1)));

		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Indicator, Arrays.asList(1,2,103));
		element.setFilter(filter);

		PivotContent content = execute(new GeneratePivotTable(element));
//		TODO real test
//		System.out.println(content.getData());
		
	}
	
	@Test
	public void adminMap() {
		
		AdministrativeLevelClustering clustering = new AdministrativeLevelClustering();
		clustering.getAdminLevels().add(1);
		
		BubbleMapLayer layer = new BubbleMapLayer();
		layer.setClustering(clustering);
		
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Site, 3);
		
		MapReportElement map = new MapReportElement();
		map.addLayer(layer);
		map.setFilter(filter);
		
		MapContent result = (MapContent)execute(new GenerateElement(map));

		System.out.println(result.getMarkers());

		MapMarker marker = result.getMarkers().get(0);
		assertThat(marker.getSiteIds().size(), equalTo(1));
		assertThat(marker.getSiteIds().get(0), equalTo(3));
		
		assertThat(result.getUnmappedSites().size(), equalTo(0));
		
		
		
	}
	
}
