package org.activityinfo.server.report.generator;

import com.google.inject.Inject;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.server.dao.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.SiteData;
import org.activityinfo.server.domain.User;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.activityinfo.test.MockHibernateModule;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import java.util.List;

@RunWith(InjectionSupport.class)
@Modules({MockHibernateModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SiteDataBinderTest {

    @Inject
    private EntityManager em;

    @Inject
    private SiteTableDAOHibernate dao;

    @Test
    public void test() {

        List<SiteData> sites = dao.query(new User(), Restrictions.eq("site.id", 2),
                null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_ALL, 0, -1);

        Assert.assertEquals("sites returned", 1, sites.size());

        SiteData site = sites.get(0);

        Assert.assertEquals("Ngshwe", site.getLocationName());
        Assert.assertNull(site.getLocationAxe());
        Assert.assertTrue("has coords", site.hasLatLong());
        Assert.assertEquals(1.323, site.getLongitude(), 0.001);
        Assert.assertEquals(28.232, site.getLatitude(), 0.001);
        Assert.assertEquals("partner", "NRC", site.getPartnerName());
        Assert.assertEquals("indicator 1", 3600, site.getIndicatorValue(1).intValue());
        Assert.assertEquals("indicator 2", 1200, site.getIndicatorValue(2).intValue());


//	<location locationId="2" name="Ngshwe" locationTypeId="1" X="1.323" Y="28.232" version="1"/>
//	<locationAdminLink locationId="2" adminEntityId="2"/>
//	<locationAdminLink locationId="2" adminEntityId="11"/>
//
//	<site siteId="2" activityId="1" locationId="2" partnerId="1" Status="1" Date1="2009-01-15" Date2="2009-01-16" DateCreated="2009-02-01" DateEdited="2009-02-01" version="1"/>
//	<attributeValue siteId="2" attributeId="1" value="1"/>
//	<attributeValue siteId="2" attributeId="2" value="1"/>
//


    }
}
