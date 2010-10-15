/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.hibernate.HibernateSiteTableDAO;
import org.sigmah.shared.dao.SiteProjectionBinder;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import javax.persistence.EntityManager;
import java.util.List;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
@Modules({MockHibernateModule.class})
public class SiteTableDAOTest {

    @Inject
    private EntityManager em;

    @Inject
    private HibernateSiteTableDAO dao;


    @Test
    public void testNullCriteria() {


        User user = em.find(User.class, 1);

        SiteProjectionBinder binder = createNiceMock(SiteProjectionBinder.class);
        replay(binder);

        List list = dao.query(user, null, null, binder, SiteTableDAO.RETRIEVE_ALL, 0, -1);
    }

}
