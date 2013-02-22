

package org.activityinfo.server.database.hibernate.dao;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static junit.framework.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;

import junit.framework.Assert;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.dao.AdminDAO;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.AssertUtils;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;


@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/adminEntities.db.xml")
@Modules({MockHibernateModule.class})
public class AdminDAOImplTest {

	@Inject
    private AdminDAO adminDAO;


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
