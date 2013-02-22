package org.activityinfo.server.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.GetSiteHistory;
import org.activityinfo.shared.command.GetSiteHistory.GetSiteHistoryResult;
import org.activityinfo.shared.dto.SiteHistoryDTO;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetSiteHistoryTest extends CommandTestCase {
    @Test
    public void testGetSiteHistory() {
        setUser(1);

        int siteId = 1;

        GetSiteHistoryResult result = execute(new GetSiteHistory(siteId));
        assertNotNull(result);
        assertEquals(2, result.getSiteHistories().size());

        SiteHistoryDTO dto1 = result.getSiteHistories().get(0);
        assertEquals(1, dto1.getId());
        assertTrue(dto1.isInitial());
        Map<String, Object> map = dto1.getJsonMap();
        assertEquals(new Integer(1), map.get("id"));
        assertEquals("1", String.valueOf(map.get("id")));
        assertEquals("54.0", String.valueOf(map.get("I4925")));
        assertEquals("site 1 my first comment", map.get("comments"));

        SiteHistoryDTO dto2 = result.getSiteHistories().get(1);
        assertEquals(2, dto2.getId());
        assertFalse(dto2.isInitial());
        map = dto2.getJsonMap();
        assertNull(map.get("id"));
        assertNull(map.get("I4925"));
        assertEquals("site 1 changed comment", map.get("comments"));

    }
}