package org.activityinfo.server.dao;

import org.activityinfo.server.DbUnitTestCase;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.User;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteTableDAOTest extends DbUnitTestCase {


    @Test
    public void testNullCriteria() {

		populate("sites-simple1");

        EntityManager em = emf.createEntityManager();
        SiteTableDAO dao = new SiteTableDAOHibernate(em);

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
