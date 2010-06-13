/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.client.dispatch.remote.cache;

import org.activityinfo.client.dispatch.remote.ProxyManager;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.junit.Assert;
import org.junit.Test;

public class AdminEntityCacheTest {


    @Test
    public void testRootLevelCache() {

        ProxyManager proxyMgr = new ProxyManager();

        new AdminEntityCache(proxyMgr);

        proxyMgr.notifyListenersOfSuccess(new GetAdminEntities(1), DummyData.getProvinces());

        CommandProxyResult<ListResult<AdminEntityDTO>> proxyResult = proxyMgr.execute(new GetAdminEntities(1));

        Assert.assertTrue(proxyResult.couldExecute);
        Assert.assertEquals(2, proxyResult.result.getData().size());
    }
}
