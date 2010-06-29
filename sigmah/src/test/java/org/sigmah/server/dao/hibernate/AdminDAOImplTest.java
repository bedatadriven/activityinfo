package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.AdminDAO;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.domain.AdminEntity;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.AssertUtils;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import java.util.Comparator;
import java.util.List;

import static junit.framework.Assert.assertEquals;


@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/adminEntities.db.xml")
@Modules({MockHibernateModule.class})
public class AdminDAOImplTest {

    private AdminDAO adminDAO;

    @Inject
    public AdminDAOImplTest(AdminHibernateDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @Test
    public void queryRoot() throws CommandException {

        List<AdminEntity> result = adminDAO.findRootEntities(1);

        Assert.assertTrue("all are present", result.size() == 4);
        assertSorted(result);
    }

    @Test
    public void queryChildren() throws CommandException {

        List<AdminEntity> result = adminDAO.findChildEntities(2, 2);

        assertEquals("count", 3, result.size());
        assertSorted(result);
        assertEquals("level", "Territoire", result.get(0).getLevel().getName());
        assertEquals("parentId", "Sud Kivu", result.get(0).getParent().getName());
    }

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void queryRootEntitiesWithSites() throws Exception {

        List<AdminEntity> result = adminDAO.query()
                .level(1)
                .withSitesOfActivityId(4)
                .execute();

        assertEquals(1, result.size());
    }

    private void assertSorted(List<AdminEntity> result) {
        AssertUtils.assertSorted("list", result, new Comparator<AdminEntity>() {
            @Override
            public int compare(AdminEntity o1, AdminEntity o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }


}
