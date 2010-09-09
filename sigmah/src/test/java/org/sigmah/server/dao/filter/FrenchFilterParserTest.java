/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.filter;

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.dao.SiteTableDAO;
import org.sigmah.server.dao.hibernate.SiteTableDAOHibernate;
import org.sigmah.server.domain.SiteData;
import org.sigmah.shared.domain.User;
import org.sigmah.server.report.generator.SiteDataBinder;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import javax.persistence.EntityManager;
import java.util.List;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/filter.db.xml")
@Modules({MockHibernateModule.class})
public class FrenchFilterParserTest {

    @Inject
    private FrenchFilterParser parser;

    @Inject
    private EntityManager em;

    @Inject
    private SiteTableDAOHibernate dao;

    private User user = new User();

    @Test
    public void testAdminFilter() {

        SiteTableDAOHibernate dao = new SiteTableDAOHibernate(em);
        List<SiteData> results = dao.query(user, parser.parse("Sud Kivu"), null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Assert.assertEquals(2, results.size());
    }

    @Test
    public void testPartnerFilter() {

        List<SiteData> results = dao.query(user, parser.parse("IRC"), null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Assert.assertEquals(2, results.size());
    }


    @Test
    public void testAdminPartnerFilter() {

        List<SiteData> results = dao.query(user, parser.parse("IRC Sud Kivu"), null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Assert.assertEquals(1, results.size());
    }

    @Test
    public void testYearFilter() {

        List<SiteData> results = dao.query(user, parser.parse("2009"), null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Assert.assertEquals(2, results.size());
    }
}
