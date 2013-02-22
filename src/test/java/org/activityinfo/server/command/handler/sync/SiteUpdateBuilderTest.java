package org.activityinfo.server.command.handler.sync;

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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.google.inject.Provider;

@RunWith(InjectionSupport.class)
@Modules({
    MockHibernateModule.class,
    LoggingModule.class
})
public class SiteUpdateBuilderTest {

    @Inject
    private Provider<SiteUpdateBuilder> builder;

    @Inject
    private EntityManagerFactory emf;

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void subsequentCallsAreUpToDate() throws Exception {

        User user = new User();
        user.setId(1);

        // update one of the sites so we have a realistic nano value type stamp
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Site site = em.find(Site.class, 1);
        site.setComments("I'm slightly new");
        site.setDateEdited(new Date());
        em.getTransaction().commit();
        em.close();

        SyncRegionUpdate initialUpdate = builder.get().build(user,
            new GetSyncRegionUpdates("sites/1/1", null));
        assertThat(initialUpdate.isComplete(), equalTo(true));
        assertThat(initialUpdate.getSql(), not(nullValue()));
        assertThat(initialUpdate.getSql(), containsString("slightly new"));
        System.out.println(initialUpdate.getSql());

        // nothing has changed!

        SyncRegionUpdate subsequentUpdate = builder.get().build(user,
            new GetSyncRegionUpdates("sites/1/1", initialUpdate.getVersion()));

        assertThat(subsequentUpdate.isComplete(), equalTo(true));
        assertThat(subsequentUpdate.getSql(), nullValue());
        assertThat(subsequentUpdate.getVersion(),
            equalTo(initialUpdate.getVersion()));
    }
}
