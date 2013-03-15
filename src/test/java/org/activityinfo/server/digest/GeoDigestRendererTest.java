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

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
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
    TestDigestModule.class
})
public class GeoDigestRendererTest {
    @Inject
    private GeoDigestRenderer geoDigestRenderer;
    @Inject
    private EntityManager em;

    @Test
    public void testFindDatabasesOwnerAndViewAndNotification() throws Exception {
        // owner & view & notification
        User user = em.find(User.class, 100);
        List<UserDatabase> dbs = geoDigestRenderer.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(2)));
        assertTrue(dbs.contains(em.find(UserDatabase.class, 3)));
        assertTrue(dbs.contains(em.find(UserDatabase.class, 100)));

        System.out.println(new Date(1362700800000L));
    }

    @Test
    public void testFindDatabasesOwnerAndNotification() throws Exception {
        // owner & notification
        User user = em.find(User.class, 1);
        List<UserDatabase> dbs = geoDigestRenderer.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(2)));
        assertTrue(dbs.contains(em.find(UserDatabase.class, 1)));
        assertTrue(dbs.contains(em.find(UserDatabase.class, 2)));
    }

    @Test
    public void testFindDatabasesOwnerAndNoNotification() throws Exception {
        // owner & no notification
        User user = em.find(User.class, 2);
        List<UserDatabase> dbs = geoDigestRenderer.findDatabases(user);
        dbs = geoDigestRenderer.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(0)));
    }

    @Test
    public void testFindDatabasesViewAndNotification() throws Exception {
        // view & notification
        User user = em.find(User.class, 3);
        List<UserDatabase> dbs = geoDigestRenderer.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(1)));
        assertTrue(dbs.contains(em.find(UserDatabase.class, 1)));
    }

    @Test
    public void testFindDatabasesOnlyNotification() throws Exception {
        // only notification
        User user = em.find(User.class, 7);
        List<UserDatabase> dbs = geoDigestRenderer.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(0)));
    }

    @Test
    public void testFindSitesNoSiteDatabase() {
        List<Integer> sites = geoDigestRenderer.findSiteIds(
            em.find(UserDatabase.class, 2), 1300000000000L);
        assertThat(sites.size(), is(equalTo(0)));
    }

    @Test
    public void testFindSitesBeforeFrom() {
        List<Integer> sites = geoDigestRenderer.findSiteIds(
            em.find(UserDatabase.class, 1), 1370000000000L);
        assertThat(sites.size(), is(equalTo(0)));
    }

    @Test
    public void testFindSitesBetween() {
        List<Integer> sites = geoDigestRenderer.findSiteIds(
            em.find(UserDatabase.class, 1), 1360000000000L);
        assertThat(sites.size(), is(equalTo(1)));
        assertTrue(sites.contains(1));
    }

    @Test
    public void testFindSitesAfterFrom() {
        List<Integer> sites = geoDigestRenderer.findSiteIds(
            em.find(UserDatabase.class, 1), 1350000000000L);
        assertThat(sites.size(), is(equalTo(2)));
        assertTrue(sites.contains(1));
        assertTrue(sites.contains(2));
    }
}