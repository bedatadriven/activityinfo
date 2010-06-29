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
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.SchemaDTO;

public class SchemaCacheTest {

    @Test
    public void testSchemaCache() {

        ProxyManager proxyMgr = new ProxyManager();

        SchemaCache cache = new SchemaCache(proxyMgr);

        SchemaDTO schema = DummyData.PEAR();

        proxyMgr.notifyListenersOfSuccess(new GetSchema(), schema);

        CommandProxyResult<SchemaDTO> proxyResult = proxyMgr.execute(new GetSchema());

        Assert.assertTrue("could execute locally", proxyResult.couldExecute);
        Assert.assertEquals("PEAR", proxyResult.result.getDatabaseById(1).getName());
    }
}