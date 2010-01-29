package org.activityinfo.clientjre.dispatch.remote.cache;

import org.activityinfo.client.dispatch.remote.ProxyManager;
import org.activityinfo.client.dispatch.remote.cache.AdminEntityCache;
import org.activityinfo.client.dispatch.remote.cache.CommandProxyResult;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.junit.Assert;
import org.junit.Test;

public class AdminEntityCacheTest {


    @Test
    public void testRootLevelCache() {

        ProxyManager proxyMgr = new ProxyManager();

        new AdminEntityCache(proxyMgr);

        proxyMgr.notifyListenersOfSuccess(new GetAdminEntities(1), DummyData.getProvinces());

        CommandProxyResult<ListResult<AdminEntityModel>> proxyResult = proxyMgr.execute(new GetAdminEntities(1));

        Assert.assertTrue(proxyResult.couldExecute);
        Assert.assertEquals(2, proxyResult.result.getData().size());
    }
}
