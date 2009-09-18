package org.activityinfo.clientjre.command;

import org.activityinfo.client.command.CommandProxyResult;
import org.activityinfo.client.command.ProxyManager;
import org.activityinfo.client.command.cache.AdminEntityCache;
import org.activityinfo.client.command.cache.SchemaCache;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.clientjre.mock.MockEventBus;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.Schema;
import org.junit.Test;
import org.junit.Assert;

public class AdminEntityCacheTest {


    @Test
    public void testRootLevelCache() {

        ProxyManager proxyMgr = new ProxyManager();
        MockEventBus eventBus = new MockEventBus();

        new AdminEntityCache(proxyMgr);

        proxyMgr.notifyListenersOfSuccess(new GetAdminEntities(1), DummyData.getProvinces());

        CommandProxyResult<ListResult<AdminEntityModel>> proxyResult = proxyMgr.execute(new GetAdminEntities(1));

        Assert.assertTrue(proxyResult.couldExecute);
        Assert.assertEquals(2, proxyResult.result.getData().size());
    }
}
