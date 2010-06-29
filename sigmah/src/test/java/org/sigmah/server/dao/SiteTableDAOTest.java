/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.hibernate.SiteTableDAOHibernate;
import org.sigmah.server.domain.AdminEntity;
import org.sigmah.server.domain.User;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import javax.persistence.EntityManager;
import java.util.List;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
@Modules({MockHibernateModule.class})
public class SiteTableDAOTest {

    @Inject
    private EntityManager em;

    @Inject
    private SiteTableDAOHibernate dao;


    @Test
    public void testNullCriteria() {

        SiteProjectionBinder binder = new MockBinder();

        User user = em.find(User.class, 1);

        List list = dao.query(user, null, null, binder, SiteTableDAO.RETRIEVE_ALL, 0, -1);
    }

    private static class MockBinder implements SiteProjectionBinder {
        @Override
        public Object newInstance(String[] properties, Object[] values) {
            return null;
        }

        @Override
        public void setAdminEntity(Object site, AdminEntity entity) {

        }

        @Override
        public void addIndicatorValue(Object site, int indicatorId, int aggregationMethod, double value) {

        }

        @Override
        public void setAttributeValue(Object site, int attributeId, boolean value) {

        }
    }
}
