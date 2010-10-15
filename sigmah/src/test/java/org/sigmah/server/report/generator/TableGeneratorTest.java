/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.server.report.generator.map.MockBaseMapDAO;
import org.sigmah.shared.dao.*;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.TableData;
import org.sigmah.shared.report.model.BubbleMapLayer;
import org.sigmah.shared.report.model.MapElement;
import org.sigmah.shared.report.model.TableColumn;
import org.sigmah.shared.report.model.TableElement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;

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

    @Test
    public void testTableGenerator() {

        TableElement table = new TableElement();
        table.addColumn(new TableColumn("Location", "location.name"));

        TableGenerator gtor = new TableGenerator(createPivotDAO(), new MockSiteTableDAO(), createIndicator(), null);
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

        TableGenerator gtor = new TableGenerator(createPivotDAO(), new MockSiteTableDAO(), createIndicator(),
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

    private IndicatorDAO createIndicator() {
        IndicatorDAO indicatorDAO = createNiceMock(IndicatorDAO.class);
        replay(indicatorDAO);
        return indicatorDAO;
    }

    private PivotDAO createPivotDAO() {
        PivotDAO pivotDAO = createNiceMock(PivotDAO.class);
        replay(pivotDAO);
        return pivotDAO;
    }

    private class MockSiteTableDAO implements SiteTableDAO {
        @Override
        public <RowT> List<RowT> query(User user, Filter filter, List<SiteOrder> orderings, SiteProjectionBinder<RowT> binder, int retrieve, int offset, int limit)  {
            try {
                final ResultSet rs = createNiceMock(ResultSet.class);
                expect(rs.getInt(SiteTableColumn.id.index())).andReturn(1);
                expect(rs.getObject(SiteTableColumn.id.index())).andReturn(1);
                expect(rs.getObject(SiteTableColumn.location_name.index())).andReturn("tampa bay");
                expect(rs.getDouble(SiteTableColumn.x.index())).andReturn(28.4);
                expect(rs.getObject(SiteTableColumn.x.index())).andReturn(28.4);
                expect(rs.getDouble(SiteTableColumn.y.index())).andReturn(1.2);
                expect(rs.getObject(SiteTableColumn.y.index())).andReturn(1.2);
                expect(rs.wasNull()).andReturn(false).anyTimes();
                replay(rs);

                return Collections.singletonList(binder.newInstance(new String[0], rs));
            } catch (SQLException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public int queryCount(User user, Filter filter) {
            return 0;
        }

        @Override
        public int queryPageNumber(User user, Filter filter, List<SiteOrder> orderings, int pageSize, int siteId) {
            return 0;
        }
    }


}
