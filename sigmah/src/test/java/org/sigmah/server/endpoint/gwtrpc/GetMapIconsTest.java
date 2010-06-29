/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.domain.User;
import org.sigmah.server.endpoint.gwtrpc.handler.GetMapIconsHandler;
import org.sigmah.server.report.ReportModule;
import org.sigmah.shared.command.GetMapIcons;
import org.sigmah.shared.command.result.MapIconResult;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

@RunWith(InjectionSupport.class)
@Modules({ReportModule.class, GwtRpcModule.class})
public class GetMapIconsTest {

    @Test
    public void testGetMapIcons() throws Exception {

        GetMapIconsHandler handler = new GetMapIconsHandler("war/mapicons");
        MapIconResult result = (MapIconResult) handler.execute(new GetMapIcons(), new User());

        Assert.assertTrue(result.getData().size() > 0);

    }
}
