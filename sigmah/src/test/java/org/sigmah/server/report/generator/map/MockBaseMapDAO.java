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

package org.sigmah.server.report.generator.map;

import org.sigmah.server.dao.BaseMapDAO;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.map.LocalBaseMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Bertram
 */
public class MockBaseMapDAO implements BaseMapDAO {

    Map<String, BaseMap> baseMaps;

    public MockBaseMapDAO() {
        baseMaps = new HashMap<String,BaseMap>();

        BaseMap map1 = new LocalBaseMap();
        map1.setId("map1");
        map1.setMinZoom(0);
        map1.setMaxZoom(12);
        map1.setCopyright("(C)");
        map1.setName("Grand Canyon");
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
