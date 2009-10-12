package org.activityinfo.clientjre.command;

import org.activityinfo.client.command.ProxyManager;
import org.activityinfo.client.command.cache.CommandProxyResult;
import org.activityinfo.client.command.cache.SchemaCache;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.clientjre.mock.MockEventBus;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.Schema;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SchemaCacheTest {



    @Test
    public void testSchemaCache() {

        ProxyManager proxyMgr = new ProxyManager();
        MockEventBus eventBus = new MockEventBus();

        SchemaCache cache = new SchemaCache(eventBus, proxyMgr);

        Schema schema = DummyData.PEAR();

        proxyMgr.notifyListenersOfSuccess(new GetSchema(), schema);

        CommandProxyResult<Schema> proxyResult = proxyMgr.execute(new GetSchema());

        Assert.assertTrue("could execute locally", proxyResult.couldExecute);
        Assert.assertEquals("PEAR", proxyResult.result.getDatabaseById(1).getName());
    }


}