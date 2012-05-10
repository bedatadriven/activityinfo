/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote.cache;

import org.activityinfo.client.dispatch.remote.ProxyManager;
import org.activityinfo.client.dispatch.remote.cache.ProxyResult;
import org.activityinfo.client.dispatch.remote.cache.SchemaCache;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.DTOs;
import org.activityinfo.shared.dto.SchemaDTO;
import org.junit.Assert;
import org.junit.Test;

public class SchemaCacheTest {

    @Test
    public void testSchemaCache() {

        ProxyManager proxyMgr = new ProxyManager();

        SchemaCache cache = new SchemaCache(proxyMgr);

        SchemaDTO schema = DTOs.PEAR();

        proxyMgr.notifyListenersOfSuccess(new GetSchema(), schema);

        ProxyResult<SchemaDTO> proxyResult = proxyMgr.execute(new GetSchema());

        Assert.assertTrue("could execute locally", proxyResult.isCouldExecute());
        Assert.assertEquals("PEAR", proxyResult.getResult().getDatabaseById(1).getName());
    }
}