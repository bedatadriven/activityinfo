package org.activityinfo.clientjre.dispatch.remote.cache;

import org.activityinfo.client.dispatch.remote.ProxyManager;
import org.activityinfo.client.dispatch.remote.cache.CommandProxyResult;
import org.activityinfo.client.dispatch.remote.cache.SchemaCache;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.Schema;
import org.junit.Assert;
import org.junit.Test;

public class SchemaCacheTest {

    @Test
    public void testSchemaCache() {

        ProxyManager proxyMgr = new ProxyManager();

        SchemaCache cache = new SchemaCache(proxyMgr);

        Schema schema = DummyData.PEAR();

        proxyMgr.notifyListenersOfSuccess(new GetSchema(), schema);

        CommandProxyResult<Schema> proxyResult = proxyMgr.execute(new GetSchema());

        Assert.assertTrue("could execute locally", proxyResult.couldExecute);
        Assert.assertEquals("PEAR", proxyResult.result.getDatabaseById(1).getName());
    }
}