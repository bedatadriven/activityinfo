/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.page.entry.place.ActivityDataEntryPlace;
import org.sigmah.client.page.entry.place.DataEntryPlaceParser;
import org.sigmah.shared.dto.ActivityDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.SortInfo;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteGridPlaceParserTest {

    @Test
    public void test() {

        PageStateSerializer pmgr = new PageStateSerializer();
        pmgr.registerParser(DataEntryPage.PAGE_ID, new DataEntryPlaceParser());

        ActivityDataEntryPlace place = new ActivityDataEntryPlace(new ActivityDTO(99, "NFI Dist"));

        String token = pmgr.serialize(place);

        ActivityDataEntryPlace pplace = (ActivityDataEntryPlace)pmgr.deserialize(token);

        Assert.assertEquals(place.getActivityId(), pplace.getActivityId());

    }

    @Test
    public void testHarder() {

        PageStateSerializer pmgr = new PageStateSerializer();
        pmgr.registerParser(DataEntryPage.PAGE_ID, new DataEntryPlaceParser());

        ActivityDataEntryPlace place = new ActivityDataEntryPlace(new ActivityDTO(99, "NFI Dist"));
        place.setPageNum(3);
        place.setSortInfo(new SortInfo("date2", Style.SortDir.DESC));

        String token = pmgr.serialize(place);
        
        System.out.println(token);

        ActivityDataEntryPlace pplace = (ActivityDataEntryPlace)pmgr.deserialize(token);

        Assert.assertEquals(place.getActivityId(), pplace.getActivityId());

    }
}
