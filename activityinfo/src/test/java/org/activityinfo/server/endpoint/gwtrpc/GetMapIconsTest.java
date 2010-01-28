package org.activityinfo.server.endpoint.gwtrpc;

import org.activityinfo.server.domain.User;
import org.activityinfo.server.endpoint.gwtrpc.handler.GetMapIconsHandler;
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
