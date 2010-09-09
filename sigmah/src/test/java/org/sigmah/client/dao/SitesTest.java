/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dao;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.client.mock.MockJPAModule;
import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({ MockJPAModule.class })
public class SitesTest extends DaoTest {
	
	@Inject
    protected AdminDAO adminDAO;
   
	@Test
    public void queryRootEntitiesWithSites() throws Exception {
        List<AdminEntity> result = adminDAO.find(1, -1, 4);
        assertEquals(1, result.size());
    }

	@Override
	public String getResourcePath() {
		return "/dbunit/client-sites-simple1.db.xml";
	}
}
