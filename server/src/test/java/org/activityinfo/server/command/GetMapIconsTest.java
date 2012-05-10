/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command;

import org.activityinfo.server.command.handler.GetMapIconsHandler;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.report.ReportModule;
import org.activityinfo.shared.command.GetMapIcons;
import org.activityinfo.shared.command.result.MapIconResult;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

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
