/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.sync;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

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
    public void subsequentCallsAreUpToDate() throws JSONException {

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

        SyncRegionUpdate initialUpdate = builder.get().build(user, new GetSyncRegionUpdates("sites/1/1", null));
        assertThat(initialUpdate.isComplete(), equalTo(true));
        assertThat(initialUpdate.getSql(), not(nullValue()));
        assertThat(initialUpdate.getSql(), containsString("slightly new"));
        System.out.println(initialUpdate.getSql());

        // nothing has changed!

        SyncRegionUpdate subsequentUpdate = builder.get().build(user,
                new GetSyncRegionUpdates("sites/1/1", initialUpdate.getVersion()));

        assertThat(subsequentUpdate.isComplete(), equalTo(true));
        assertThat(subsequentUpdate.getSql(), nullValue());
        assertThat(subsequentUpdate.getVersion(), equalTo(initialUpdate.getVersion()));
    }

}
