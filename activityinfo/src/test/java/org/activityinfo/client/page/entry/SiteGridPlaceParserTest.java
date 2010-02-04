package org.activityinfo.client.page.entry;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.SortInfo;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.entry.SiteGridPlace;
import org.activityinfo.shared.dto.ActivityModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteGridPlaceParserTest {

    @Test
    public void test() {

        PlaceSerializer pmgr = new PlaceSerializer();
        pmgr.registerParser(Pages.SiteGrid, new SiteGridPlace.Parser());

        SiteGridPlace place = new SiteGridPlace(new ActivityModel(99, "NFI Dist"));

        String token = pmgr.serialize(place);

        SiteGridPlace pplace = (SiteGridPlace)pmgr.deserialize(token);

        Assert.assertEquals(place.getActivityId(), pplace.getActivityId());

    }

    @Test
    public void testHarder() {

        PlaceSerializer pmgr = new PlaceSerializer();
        pmgr.registerParser(Pages.SiteGrid, new SiteGridPlace.Parser());

        SiteGridPlace place = new SiteGridPlace(new ActivityModel(99, "NFI Dist"));
        place.setPageNum(3);
        place.setSortInfo(new SortInfo("date2", Style.SortDir.DESC));

        String token = pmgr.serialize(place);
        
        System.out.println(token);

        SiteGridPlace pplace = (SiteGridPlace)pmgr.deserialize(token);

        Assert.assertEquals(place.getActivityId(), pplace.getActivityId());

    }
}
