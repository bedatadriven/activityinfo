package org.activityinfo.server.digest;

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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/digests.db.xml")
@Modules({
    TestDatabaseModule.class,
    MockHibernateModule.class,
})
public class DigestResourceTest {
    @Inject
    private DigestResource digestResource;

    @Test
    public void testUserSelection() throws Exception {
        List<Integer> users = digestResource.selectUsers();
        assertThat(users.size(), is(equalTo(4)));
        assertTrue(users.contains(1));
        assertTrue(users.contains(3));
        assertTrue(users.contains(5));
        assertTrue(users.contains(100));
    }
}