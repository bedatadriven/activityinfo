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
import org.sigmah.shared.map.BaseMap;

/**
 * @author Alex Bertram
 */
public class MockBaseMapDAO implements BaseMapDAO {

    Map<String, BaseMap> baseMaps;

    public MockBaseMapDAO() {
        baseMaps = new HashMap<String,BaseMap>();

        BaseMap map1 = new BaseMap();
        map1.setId("map1");
        map1.setMinZoom(0);
        map1.setMaxZoom(12);
        map1.setCopyright("(C)");
        map1.setName("Grand Canyon");
        map1.setTileUrlPattern("http://s/test.png");
        baseMaps.put("map1", map1);

    }

    @Override
    public BaseMap getBaseMap(String id) {
        return baseMaps.get(id);
    }

    @Override
    public List<BaseMap> getBaseMaps() {
        return new ArrayList<BaseMap>(baseMaps.values());
    }
}
