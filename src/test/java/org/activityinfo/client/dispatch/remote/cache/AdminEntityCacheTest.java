/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote.cache;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.dispatch.remote.cache.AdminEntityCache;
import org.activityinfo.client.dispatch.remote.cache.CacheResult;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.DTOs;
import org.junit.Assert;
import org.junit.Test;

public class AdminEntityCacheTest {


    @Test
    public void testRootLevelCache() {

        CacheManager proxyMgr = new CacheManager(new MockEventBus());

        new AdminEntityCache(proxyMgr);

        proxyMgr.notifyListenersOfSuccess(new GetAdminEntities(1), DTOs.getProvinces());

        CacheResult<ListResult<AdminEntityDTO>> proxyResult = proxyMgr.execute(new GetAdminEntities(1));

        Assert.assertTrue(proxyResult.isCouldExecute());
        Assert.assertEquals(2, proxyResult.getResult().getData().size());
    }
}
