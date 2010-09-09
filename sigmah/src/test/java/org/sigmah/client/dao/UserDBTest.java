/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dao;

import static junit.framework.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.client.mock.MockJPAModule;
import org.sigmah.shared.dao.UserDatabaseDAO;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;
import com.google.inject.Inject;
// this test only works with sqlite
@Ignore
@RunWith(InjectionSupport.class)
@Modules({ MockJPAModule.class })
public class UserDBTest extends DaoTest {
	
	@Inject
    protected UserDatabaseDAO dbDAO;
   
	@Test
    public void testAct() throws Exception {
        UserDatabase result = dbDAO.findById(1);
        assertEquals(result.getActivities().size(), 10);
    }

	@Override
	public String getResourcePath() {
		return "/dbunit/client-sites-simple1.db.xml";
	}
}
