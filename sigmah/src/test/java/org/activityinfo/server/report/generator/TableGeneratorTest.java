/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.report.generator;

import junit.framework.Assert;
import org.activityinfo.server.dao.*;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.map.MockBaseMapDAO;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.BubbleMapLayer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.TableColumn;
import org.activityinfo.shared.report.model.TableElement;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

/**
 * @author Alex Bertram
 */
public class TableGeneratorTest {
    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setName("Alex");
        user.setEmail("akbertra@mgail.com");
        user.setLocale("fr");
    }


    public class MockSiteTableDAO implements SiteTableDAO {

        @Override
        public <RowT> List<RowT> query(User user, Criterion criterion, List<Order> orderings, SiteProjectionBinder<RowT> binder, int retrieve, int offset, int limit) {
            List<RowT> rows = new ArrayList<RowT>();
            Object[] row = new Object[SiteColumn.values().length];
            String[] properties = new String[SiteColumn.values().length];

            row[0] = 1;
            row[SiteColumn.location_name.index()] = "tampa bay";
            row[SiteColumn.x.index()] = 28.4;
            row[SiteColumn.y.index()] = 1.2;

            rows.add(binder.newInstance(properties, row));
            return rows;
        }

        @Override
        public int queryCount(Conjunction criterion) {
            return 0;
        }

        @Override
        public int queryPageNumber(User user, Criterion criterion, List<Order> orderings, int pageSize, int siteId) {
            return 0;
        }
    }

    private SchemaDAO createSchemaDAO() {
        SchemaDAO schemaDAO = createNiceMock(SchemaDAO.class);
        replay(schemaDAO);
        return schemaDAO;
    }

    private PivotDAO createPivotDAO() {
        PivotDAO pivotDAO = createNiceMock(PivotDAO.class);
        replay(pivotDAO);
        return pivotDAO;
    }

    @Test
    public void testTableGenerator() {

        TableElement table = new TableElement();
        table.addColumn(new TableColumn("Location", "location.name"));

        TableGenerator gtor = new TableGenerator(createPivotDAO(), new MockSiteTableDAO(), createSchemaDAO(), null);
        gtor.generate(user, table, null, null);

        Assert.assertNotNull("content is set", table.getContent());

        TableData data = table.getContent().getData();
        List<TableData.Row> rows = data.getRows();
        Assert.assertEquals("row count", 1, rows.size());

        TableData.Row row = rows.get(0);
        Assert.assertEquals("column data", "tampa bay", row.values[0]);
    }


    @Test
    public void testMap() {

        TableElement table = new TableElement();
        table.addColumn(new TableColumn("Index", "map"));
        table.addColumn(new TableColumn("Site", "location.name"));

        MapElement map = new MapElement();
        map.setBaseMapId("map1");
        BubbleMapLayer layer = new BubbleMapLayer();
        layer.setNumbering(BubbleMapLayer.NumberingType.ArabicNumerals);
        map.addLayer(layer);
        table.setMap(map);

        TableGenerator gtor = new TableGenerator(createPivotDAO(), new MockSiteTableDAO(), createSchemaDAO(),
                new MapGenerator(createPivotDAO(), new MockSiteTableDAO(), new MockBaseMapDAO()));
        gtor.generate(user, table, null, null);

        MapContent mapContent = map.getContent();
        Assert.assertNotNull("map content", mapContent);
        Assert.assertEquals("marker count", 1, mapContent.getMarkers().size());
        Assert.assertEquals("label on marker", "1", ((BubbleMapMarker) mapContent.getMarkers().get(0)).getLabel());

        Map<Integer, String> siteLabels = mapContent.siteLabelMap();
        Assert.assertEquals("site id in map", "1", siteLabels.get(1));

        TableData.Row row = table.getContent().getData().getRows().get(0);
        Assert.assertEquals("label on row", "1", row.values[0]);
    }

}
