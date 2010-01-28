package org.activityinfo.server.dao;

import com.google.inject.Inject;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.User;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.activityinfo.test.TestingHibernateModule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import java.util.List;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
@Modules({TestingHibernateModule.class})
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
