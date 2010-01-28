package org.activityinfo.server.endpoint.gwtrpc;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.SortInfo;
import junit.framework.Assert;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.dto.IndicatorModel;
import org.activityinfo.shared.dto.SiteModel;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetSitesTest extends CommandTestCase {
    private static final int DATABASE_OWNER = 1;


    @Test
    public void testActivityQueryBasic() throws CommandException {


        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.setActivityId(1);
        cmd.setSortInfo(new SortInfo("date2", SortDir.DESC));

        PagingLoadResult<SiteModel> result = execute(cmd);

        Assert.assertEquals("totalLength", 3, result.getData().size());
        Assert.assertEquals("totalLength", 3, result.getTotalLength());
        Assert.assertEquals("offset", 0, result.getOffset());
        //Assert.assertNull("row(0).activity", result.getData().get(0).getActivity());

        // assure sorted
        Assert.assertEquals("sorted", 2, result.getData().get(0).getId());
        Assert.assertEquals("sorted", 1, result.getData().get(1).getId());
        Assert.assertEquals("sorted", 3, result.getData().get(2).getId());

        // assure indicators are present (site id=3)
        SiteModel s = result.getData().get(2);

        Assert.assertEquals("entityName", "Ituri", s.getAdminEntity(1).getName());
        Assert.assertNotNull("admin bounds", s.getAdminEntity(1).getBounds());
        Assert.assertEquals("indicator", 10000.0, s.getIndicatorValue(1));
    }

    @Test
    public void testIndicatorSort() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.setActivityId(1);
        cmd.setSortInfo(new SortInfo(IndicatorModel.getPropertyName(1), SortDir.DESC));

        PagingLoadResult<SiteModel> result = execute(cmd);

        // assure sorted
        Assert.assertEquals("sorted", 10000.0, result.getData().get(0).getIndicatorValue(1));
        Assert.assertEquals("sorted", 3600.0, result.getData().get(1).getIndicatorValue(1));
        Assert.assertEquals("sorted", 1500.0, result.getData().get(2).getIndicatorValue(1));

        Assert.assertNotNull("activityId", result.getData().get(0).getActivityId());
    }


    @Test
    public void testActivityQueryPaged() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.setActivityId(1);
        cmd.setSortInfo(new SortInfo(IndicatorModel.getPropertyName(1), SortDir.DESC));
        cmd.setLimit(2);
        cmd.setOffset(0);

        PagingLoadResult<SiteModel> result = execute(cmd);

        Assert.assertEquals("rows retrieved [0,2]", 2, result.getData().size());
        Assert.assertEquals("total rows [0,2]", 3, result.getTotalLength());

        cmd.setOffset(1);
        cmd.setLimit(2);

        result = execute(cmd);

        Assert.assertEquals("offset [1,2]", 1, result.getOffset());
        Assert.assertEquals("rows retrieved [1,2]", 2, result.getData().size());
        Assert.assertEquals("total rows [1,2]", 3, result.getTotalLength());

        cmd.setOffset(0);
        cmd.setLimit(50);

        result = execute(cmd);

        Assert.assertEquals("offset [0,50]", 0, result.getOffset());
        Assert.assertEquals("rows retrieved [0,50]", 3, result.getData().size());
        Assert.assertEquals("total rows [0,50]", 3, result.getTotalLength());

    }


    @Test
    public void testDatabase() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.setDatabaseId(2);

        PagingLoadResult<SiteModel> result = execute(cmd);

        Assert.assertEquals("rows", 3, result.getData().size());
        Assert.assertNotNull("activityId", result.getData().get(0).getActivityId());

    }

    @Test
    public void testDatabasePaged() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.setDatabaseId(1);
        cmd.setLimit(2);

        PagingLoadResult<SiteModel> result = execute(cmd);


        Assert.assertEquals("rows", 2, result.getData().size());

    }

    @Test
    public void testDatabasePartner2PartnerVisibility() throws CommandException {

        setUser(2); // BAVON (can't see other partner's stuff)

        GetSites cmd = new GetSites();
        cmd.setDatabaseId(1);

        PagingLoadResult<SiteModel> result = execute(cmd);

        Assert.assertEquals("rows", 3, result.getData().size());
    }

    @Test
    public void testAll() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();

        PagingLoadResult<SiteModel> result = execute(cmd);

        Assert.assertEquals("rows", 8, result.getData().size());
        Assert.assertNotNull("activityId", result.getData().get(0).getActivityId());

    }

    @Test
    public void testAllWithRemovedUser() throws CommandException {

        setUser(5); // Christian (Bad guy!)

        PagingLoadResult<SiteModel> result = execute(new GetSites());

        Assert.assertEquals("rows", 0, result.getData().size());

    }

    @Test
    public void testSeekSite() throws Exception {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.setActivityId(1);
        cmd.setSortInfo(new SortInfo(IndicatorModel.getPropertyName(1), SortDir.DESC));
        cmd.setLimit(2);
        cmd.setSeekToSiteId(1);

        PagingLoadResult<SiteModel> result = execute(cmd);

        Assert.assertEquals("second page returned", 2, result.getOffset());
        Assert.assertEquals("rows on this page", 1, result.getData().size());
        Assert.assertEquals("correct site returned", 1, result.getData().get(0).getId());
    }
}
