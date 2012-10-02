/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.test.InjectionSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.SortInfo;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetSitesTest extends CommandTestCase2{
    private static final int DATABASE_OWNER = 1;
                

    @Test
    public void testActivityQueryBasic() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo("date2", SortDir.DESC));

        PagingLoadResult<SiteDTO> result = execute(cmd);    

        Assert.assertEquals("totalLength", 3, result.getData().size());
        Assert.assertEquals("totalLength", 3, result.getTotalLength());
        Assert.assertEquals("offset", 0, result.getOffset());
        //Assert.assertNull("row(0).activity", result.getData().get(0).getActivity());

        // assure sorted
        Assert.assertEquals("sorted", 2, result.getData().get(0).getId());
        Assert.assertEquals("sorted", 1, result.getData().get(1).getId());
        Assert.assertEquals("sorted", 3, result.getData().get(2).getId());

        // assure indicators are present (site id=3)
        SiteDTO s = result.getData().get(2);

        Assert.assertEquals("entityName", "Ituri", s.getAdminEntity(1).getName());
        Assert.assertNotNull("admin bounds", s.getAdminEntity(1).getBounds());
        Assert.assertEquals("indicator", 10000.0, s.getIndicatorValue(1));
        Assert.assertNull("site x", s.getX());
        
        // assure project is present
        SiteDTO s1 = result.getData().get(1);
        assertThat(s1.getId(), equalTo(1));
        assertThat(s1.getProject().getId(), equalTo(1));
        
    }

    @Test
    public void testIndicatorSort() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo(IndicatorDTO.getPropertyName(1), SortDir.DESC));

        PagingLoadResult<SiteDTO> result = execute(cmd);

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
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo(IndicatorDTO.getPropertyName(1), SortDir.DESC));
        cmd.setLimit(2);
        cmd.setOffset(0);

        PagingLoadResult<SiteDTO> result = execute(cmd);

        assertThat("offset", result.getOffset(), equalTo(0));
        

        cmd.setOffset(1);
        cmd.setLimit(2);

        result = execute(cmd);

        assertThat(result.getOffset(), equalTo(1));
        assertThat(result.getData().size(), equalTo(2));
        assertThat("total length", result.getTotalLength(), equalTo(3));

        cmd.setOffset(0);
        cmd.setLimit(50);

        result = execute(cmd);

        assertThat(result.getOffset(), equalTo(0));
        assertThat(result.getData().size(), equalTo(3));
        assertThat("total length", result.getTotalLength(), equalTo(3));


    }


    @Test
    public void testDatabase() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.filter().addRestriction(DimensionType.Database, 2);

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("rows", 3, result.getData().size());
        Assert.assertNotNull("activityId", result.getData().get(0).getActivityId());

    }

    @Test
    public void testDatabasePaged() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.getFilter().addRestriction(DimensionType.Database, 1);
        cmd.setLimit(2);

        PagingLoadResult<SiteDTO> result = execute(cmd);


        Assert.assertEquals("rows", 2, result.getData().size());

    }

    @Test
    public void testDatabasePartner2PartnerVisibility() throws CommandException {

        setUser(2); // BAVON (can't see other partner's stuff)

        GetSites cmd = new GetSites();
        cmd.getFilter().addRestriction(DimensionType.Database, 1);

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("rows", 3, result.getData().size());
    }

    @Test
    public void testAll() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("rows", 8, result.getData().size());
        Assert.assertNotNull("activityId", result.getData().get(0).getActivityId());
    }

    @Test
    public void testAllWithRemovedUser() throws CommandException {

        setUser(5); // Christian (Bad guy!)

        PagingLoadResult<SiteDTO> result = execute(new GetSites());

        Assert.assertEquals("rows", 0, result.getData().size());
    }
    
    @Test
    public void filterByIndicator() throws CommandException {
    	setUser(1);
    	
    	Filter filter = new Filter();
    	filter.addRestriction(DimensionType.Indicator, 5);
    	
    	SiteResult result = execute(new GetSites(filter));
    	
    	assertThat(result.getData().size(), equalTo(1));
    	assertThat(result.getData().get(0).getId(), equalTo(9));
    }

    @Test
    @Ignore
    public void testSeekSite() throws Exception {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo(IndicatorDTO.getPropertyName(1), SortDir.DESC));
        cmd.setLimit(2);
        cmd.setSeekToSiteId(1);
        
        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("second page returned", 2, result.getOffset());
        Assert.assertEquals("rows on this page", 1, result.getData().size());
        Assert.assertEquals("correct site returned", 1, result.getData().get(0).getId());
    }
    

	@Test
	@OnDataSet("/dbunit/sites-public.db.xml")
	public void testGetPublicSites() throws CommandException {
		int anonnoymsUserID = 0;
		setUser(anonnoymsUserID);

		GetSites cmd = new GetSites();
		cmd.filter().onActivity(1);
		cmd.setSortInfo(new SortInfo("date2", SortDir.DESC));

		PagingLoadResult<SiteDTO> result = execute(cmd);

		Assert.assertEquals("totalLength", 3, result.getData().size());
		Assert.assertEquals("totalLength", 3, result.getTotalLength());
		Assert.assertEquals("offset", 0, result.getOffset());
		// Assert.assertNull("row(0).activity",
		// result.getData().get(0).getActivity());

		// assure sorted
		Assert.assertEquals("sorted", 2, result.getData().get(0).getId());
		Assert.assertEquals("sorted", 1, result.getData().get(1).getId());
		Assert.assertEquals("sorted", 3, result.getData().get(2).getId());

		// assure indicators are present (site id=3)
		SiteDTO s = result.getData().get(2);

		Assert.assertEquals("entityName", "Ituri", s.getAdminEntity(1)
				.getName());
		Assert.assertNotNull("admin bounds", s.getAdminEntity(1).getBounds());
		Assert.assertEquals("indicator", 10000.0, s.getIndicatorValue(1));
		Assert.assertNull("site x", s.getX());

		// assure project is present
		SiteDTO s1 = result.getData().get(1);
		assertThat(s1.getId(), equalTo(1));
		assertThat(s1.getProject().getId(), equalTo(1));
	}
	
	@Test
	public void filterOnPartner() {
		setUser(1);
		
		GetSites cmd = new GetSites();
		cmd.filter().addRestriction(DimensionType.Project, 2);

		SiteResult result = execute(cmd);
		
		assertThat(result.getData().size(), equalTo(1));
	}
	
	@Test
	@OnDataSet("/dbunit/sites-linked.db.xml")
	public void linkedSites() {
		setUser(1);
		
		GetSites cmd = new GetSites();
		cmd.filter().addRestriction(DimensionType.Activity, 1);
		cmd.setSortInfo(new SortInfo("locationName", SortDir.ASC));
		
		SiteResult result = execute(cmd);
		assertThat(result.getData().size(), equalTo(2));
		
		SiteDTO site1 = result.getData().get(0);
		SiteDTO site2 = result.getData().get(1);
		
		System.out.println(site1.getProperties());
		System.out.println(site2.getProperties());
		
		assertThat(site1.getId(), equalTo(1));
		assertThat(site1.getLocationName(), equalTo("Penekusu Kivu"));
		assertThat(site1.getActivityId(), equalTo(1));
		assertThat(site1.getIndicatorValue(1), equalTo(1500d));

		assertThat(site2.getId(), equalTo(2));
		assertThat(site2.getLocationName(), equalTo("Penekusu Kivu 2"));
		assertThat(site2.getActivityId(), equalTo(1));
		assertThat(site2.getIndicatorValue(1), equalTo(400d));
	}
	
	@Test
	@OnDataSet("/dbunit/sites-linked.db.xml")
	public void linkedSitesFilteredByIndicator() {
		setUser(1);
		
		GetSites cmd = new GetSites();
		cmd.filter().addRestriction(DimensionType.Indicator, 1);
		cmd.setSortInfo(new SortInfo("locationName", SortDir.ASC));
		
		SiteResult result = execute(cmd);
		assertThat(result.getData().size(), equalTo(2));
		
		SiteDTO site1 = result.getData().get(0);
		SiteDTO site2 = result.getData().get(1);
		
		System.out.println(site1.getProperties());
		System.out.println(site2.getProperties());
		
		assertThat(site1.getId(), equalTo(1));
		assertThat(site1.getLocationName(), equalTo("Penekusu Kivu"));
		assertThat(site1.getActivityId(), equalTo(1));
		assertThat(site1.getIndicatorValue(1), equalTo(1500d));

		assertThat(site2.getId(), equalTo(2));
		assertThat(site2.getLocationName(), equalTo("Penekusu Kivu 2"));
		assertThat(site2.getActivityId(), equalTo(1));
		assertThat(site2.getIndicatorValue(1), equalTo(400d));
	}
	
	@Test
	@OnDataSet("/dbunit/sites-linked.db.xml")
	public void duplicated() {
		
		// A given site can be appear multiple times in the list if it is the source
		// of one or more indicators
		
		setUser(1);
		
		GetSites cmd = new GetSites();
		
		SiteResult result = execute(cmd);
		assertThat(result.getData().size(), equalTo(3));
	
	}
}
