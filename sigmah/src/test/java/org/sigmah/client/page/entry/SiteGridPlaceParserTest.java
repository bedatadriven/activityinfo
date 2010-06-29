package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.SortInfo;
import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.shared.dto.ActivityDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteGridPlaceParserTest {

    @Test
    public void test() {

        PageStateSerializer pmgr = new PageStateSerializer();
        pmgr.registerParser(SiteEditor.ID, new SiteGridPageState.Parser());

        SiteGridPageState place = new SiteGridPageState(new ActivityDTO(99, "NFI Dist"));

        String token = pmgr.serialize(place);

        SiteGridPageState pplace = (SiteGridPageState)pmgr.deserialize(token);

        Assert.assertEquals(place.getActivityId(), pplace.getActivityId());

    }

    @Test
    public void testHarder() {

        PageStateSerializer pmgr = new PageStateSerializer();
        pmgr.registerParser(SiteEditor.ID, new SiteGridPageState.Parser());

        SiteGridPageState place = new SiteGridPageState(new ActivityDTO(99, "NFI Dist"));
        place.setPageNum(3);
        place.setSortInfo(new SortInfo("date2", Style.SortDir.DESC));

        String token = pmgr.serialize(place);
        
        System.out.println(token);

        SiteGridPageState pplace = (SiteGridPageState)pmgr.deserialize(token);

        Assert.assertEquals(place.getActivityId(), pplace.getActivityId());

    }
}
