/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.remote.cache;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.dispatch.remote.ProxyManager;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.result.ListResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.DTOs;

public class AdminEntityCacheTest {


    @Test
    public void testRootLevelCache() {

        ProxyManager proxyMgr = new ProxyManager();

        new AdminEntityCache(proxyMgr);

        proxyMgr.notifyListenersOfSuccess(new GetAdminEntities(1), DTOs.getProvinces());

        ProxyResult<ListResult<AdminEntityDTO>> proxyResult = proxyMgr.execute(new GetAdminEntities(1));

        Assert.assertTrue(proxyResult.isCouldExecute());
        Assert.assertEquals(2, proxyResult.getResult().getData().size());
    }
}
