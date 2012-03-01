/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.command.DispatcherSync;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.GetBaseMaps;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.BaseMapResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.map.TileBaseMap;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.TableData;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.TableColumn;
import org.sigmah.shared.report.model.TableElement;
import org.sigmah.shared.report.model.labeling.ArabicNumberSequence;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.CircledMapLayer;

/**
 * @author Alex Bertram
 */
public class TableGeneratorTest {
	private static final int INDICATOR_ID = 99;
	private User user;

	@Before
	public void setUp() {
		user = new User();
		user.setName("Alex");
		user.setEmail("akbertra@mgail.com");
		user.setLocale("fr");
	}

	@Test
	public void simpleTable() {

		TableElement table = new TableElement();
		TableColumn column = new TableColumn("Location", "location.name");
		table.addColumn(column);

		TableGenerator gtor = new TableGenerator(createDispatcher(), null);
		gtor.generate(user, table, null, null);

		Assert.assertNotNull("content is set", table.getContent());

		TableData data = table.getContent().getData();
		List<SiteDTO> rows = data.getRows();
		Assert.assertEquals("row count", 1, rows.size());

		SiteDTO row = rows.get(0);
		assertThat((String)row.get(column.getSitePropertyName()), equalTo("tampa bay"));
	}
//
//	@Test
//	public void tableWithMap() {
//		
//		MapReportElement map = new MapReportElement();
//		map.setBaseMapId(GoogleBaseMap.ROADMAP.getId());
//		
//		BubbleMapLayer layer = new BubbleMapLayer();
//		layer.addIndicator(INDICATOR_ID);
//		map.addLayer(layer);
//		
//		TableElement table = new TableElement();
//		table.setMap(map);
//	
//		TableColumn column = new TableColumn("Location", "location.name");
//		table.addColumn(column);
//		
//		TableColumn mapColumn = new TableColumn("Map", "map");
//		table.addColumn(mapColumn);
//		
//		DispatcherSync dispatcher = createDispatcher();
//		TableGenerator gtor = new TableGenerator(dispatcher, new MapGenerator(dispatcher, null, null));
//		gtor.generate(user, table, null, null);
//
//		Assert.assertNotNull("content is set", table.getContent());
//
//		TableData data = table.getContent().getData();
//		List<SiteDTO> rows = data.getRows();
//		Assert.assertEquals("row count", 1, rows.size());
//
//		SiteDTO row = rows.get(0);
//		assertThat((String)row.get(column.getSitePropertyName()), equalTo("tampa bay"));
//		assertThat((String)row.get("map"), equalTo("1"));
//	}


	@Test
	public void testMap() {

		TableElement table = new TableElement();
		table.addColumn(new TableColumn("Index", "map"));
		table.addColumn(new TableColumn("Site", "location.name"));

		MapReportElement map = new MapReportElement();
		map.setBaseMapId("map1");
		CircledMapLayer layer = new BubbleMapLayer();
		layer.setLabelSequence(new ArabicNumberSequence());
		map.addLayer(layer);
		table.setMap(map);

		DispatcherSync dispatcher = createMock(DispatcherSync.class);
		expect(dispatcher.execute(isA(GetSites.class)))
		.andReturn(new SiteResult(dummySite()))
		.anyTimes();
	
        TileBaseMap baseMap1 = new TileBaseMap();
        baseMap1.setId("map1");
        baseMap1.setMinZoom(0);
        baseMap1.setMaxZoom(12);
        baseMap1.setCopyright("(C)");
        baseMap1.setName("Grand Canyon");
        baseMap1.setTileUrlPattern("http://s/test.png");
		
		expect(dispatcher.execute(isA(GetBaseMaps.class)))
			.andReturn(new BaseMapResult(Collections.singletonList(baseMap1)));

		replay(dispatcher);

		TableGenerator gtor = new TableGenerator(dispatcher, new MapGenerator(dispatcher,  new MockIndicatorDAO()));
		gtor.generate(user, table, null, null);

		MapContent mapContent = map.getContent();
		Assert.assertNotNull("map content", mapContent);
		Assert.assertEquals("marker count", 1, mapContent.getMarkers().size());
		Assert.assertEquals("label on marker", "1", ((BubbleMapMarker) mapContent.getMarkers().get(0)).getLabel());

		Map<Integer, String> siteLabels = mapContent.siteLabelMap();
		Assert.assertEquals("site id in map", "1", siteLabels.get(1));

		SiteDTO row = table.getContent().getData().getRows().get(0);
		Assert.assertEquals("label on row", "1", row.get("map"));
	}

	private DispatcherSync createDispatcher() {
		DispatcherSync dispatcher = createMock(DispatcherSync.class);
		expect(dispatcher.execute(isA(GetSites.class))).andReturn(new SiteResult(dummySite())).anyTimes();
		replay(dispatcher);
		return dispatcher;
	}

	public SiteDTO dummySite() {
		SiteDTO site = new SiteDTO();
		site.setId(1);
		site.setLocationName("tampa bay");
		site.setIndicatorValue(INDICATOR_ID, 1500d);
		site.setX(28.4);
		site.setY(1.2);
		return site;
	}
}
