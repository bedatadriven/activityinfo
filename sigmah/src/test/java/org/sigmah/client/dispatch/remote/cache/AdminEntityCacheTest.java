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

package org.sigmah.client.dispatch.remote.cache;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.dispatch.remote.ProxyManager;
import org.sigmah.client.mock.DummyData;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.result.ListResult;
import org.sigmah.shared.dto.AdminEntityDTO;

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
