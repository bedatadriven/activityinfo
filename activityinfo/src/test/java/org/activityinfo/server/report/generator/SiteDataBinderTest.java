package org.activityinfo.server.report.generator;

import org.junit.Test;
import org.junit.Assert;
import org.activityinfo.server.DbUnitTestCase;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.SiteDataBinder;
import org.activityinfo.shared.report.content.SiteData;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class SiteDataBinderTest extends DbUnitTestCase {

    @Test
    public void test(){

        populate("sites-simple1");

        EntityManager em = emf.createEntityManager();

        SiteTableDAOHibernate dao = new SiteTableDAOHibernate(em);



        List<SiteData> sites = dao.query(new User(), Restrictions.eq("site.id",2),
                null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_ALL, 0, -1);

        Assert.assertEquals("sites returned", 1,sites.size());

        SiteData site = sites.get(0);

        Assert.assertEquals("Ngshwe", site.getLocationName());
        Assert.assertNull(site.getLocationAxe());
        Assert.assertTrue("has coords", site.hasLatLong());
        Assert.assertEquals(1.323,site.getLongitude());
        Assert.assertEquals(28.232,site.getLatitude());
        Assert.assertEquals("partner", "NRC", site.getPartnerName());
        Assert.assertEquals("indicator 1", 3600.0, site.getIndicatorValue(1));
        Assert.assertEquals("indicator 2", 1200.0, site.getIndicatorValue(2));
                


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
