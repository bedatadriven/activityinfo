/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dao;

import static junit.framework.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.client.mock.MockJPAModule;
import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.AssertUtils;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({ MockJPAModule.class })
public class AdminDAOTest extends DaoTest {

	@Inject
	protected AdminDAO adminDAO;

	public String getResourcePath() {
		return "/dbunit/adminEntities.db.xml";
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
		assertEquals("parentId", "Sud Kivu", result.get(0).getParent()
				.getName());
	}

	@Test
	public void testGetChildren() throws CommandException {
		List<AdminEntity> result = adminDAO.findChildEntities(2, 2);
		AdminEntity e = result.get(0);
		Set<AdminEntity> c =  e.getChildren();
		
		// attached collections not supported yet in rebar
		//Assert.assertTrue(c.size() > 0);
		
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