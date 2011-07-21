/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.server.dao.BaseMapDAO;
import org.sigmah.shared.map.TileBaseMap;

/**
 * @author Alex Bertram
 */
public class MockBaseMapDAO implements BaseMapDAO {

    Map<String, TileBaseMap> baseMaps;

    public MockBaseMapDAO() {
        baseMaps = new HashMap<String,TileBaseMap>();

        TileBaseMap map1 = new TileBaseMap();
        map1.setId("map1");
        map1.setMinZoom(0);
        map1.setMaxZoom(12);
        map1.setCopyright("(C)");
        map1.setName("Grand Canyon");
        map1.setTileUrlPattern("http://s/test.png");
        baseMaps.put("map1", map1);

    }

    @Override
    public TileBaseMap getBaseMap(String id) {
        return baseMaps.get(id);
    }

    @Override
    public List<TileBaseMap> getBaseMaps() {
        return new ArrayList<TileBaseMap>(baseMaps.values());
    }
}
