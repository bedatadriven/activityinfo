package org.activityinfo.server.dao.filter;

import org.activityinfo.server.DbUnitTestCase;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.SiteDataBinder;
import org.activityinfo.shared.report.content.SiteData;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class FrenchFilterParserTest extends DbUnitTestCase {


    @Test
    public void testAdminFilter() {

        populate("filter");

        User user = new User();

        EntityManager em = emf.createEntityManager();
        FrenchFilterParser parser = new FrenchFilterParser(em);

        SiteTableDAOHibernate dao = new SiteTableDAOHibernate(em);
        List<SiteData> results = dao.query(user, parser.parse("Sud Kivu"), null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Assert.assertEquals(2, results.size());


    }

    @Test
    public void testPartnerFilter() {

        populate("filter");

        User user = new User();

        EntityManager em = emf.createEntityManager();
        FrenchFilterParser parser = new FrenchFilterParser(em);

        SiteTableDAOHibernate dao = new SiteTableDAOHibernate(em);
        List<SiteData> results = dao.query(user, parser.parse("IRC"), null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Assert.assertEquals(2, results.size());

    }


    @Test
    public void testAdminPartnerFilter() {

        populate("filter");

        User user = new User();

        EntityManager em = emf.createEntityManager();
        FrenchFilterParser parser = new FrenchFilterParser(em);

        SiteTableDAOHibernate dao = new SiteTableDAOHibernate(em);
        List<SiteData> results = dao.query(user, parser.parse("IRC Sud Kivu"), null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Assert.assertEquals(1, results.size());

    }

    @Test
    public void testYearFilter() {
        populate("filter");

        User user = new User();

        EntityManager em = emf.createEntityManager();
        FrenchFilterParser parser = new FrenchFilterParser(em);

        SiteTableDAOHibernate dao = new SiteTableDAOHibernate(em);
        List<SiteData> results = dao.query(user, parser.parse("2009"), null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Assert.assertEquals(2, results.size());

    }




}
