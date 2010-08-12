/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.remote.cache;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.dispatch.remote.ProxyManager;
import org.sigmah.client.mock.DummyData;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.SchemaDTO;

public class SchemaCacheTest {

    @Test
    public void testSchemaCache() {

        ProxyManager proxyMgr = new ProxyManager();

        SchemaCache cache = new SchemaCache(proxyMgr);

        SchemaDTO schema = DummyData.PEAR();

        proxyMgr.notifyListenersOfSuccess(new GetSchema(), schema);

        ProxyResult<SchemaDTO> proxyResult = proxyMgr.execute(new GetSchema());

        Assert.assertTrue("could execute locally", proxyResult.couldExecute);
        Assert.assertEquals("PEAR", proxyResult.result.getDatabaseById(1).getName());
    }
}