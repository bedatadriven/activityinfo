package org.activityinfo.server.endpoint.gwtrpc;

import org.activityinfo.server.domain.User;
import org.activityinfo.server.endpoint.gwtrpc.handler.GetMapIconsHandler;
import org.activityinfo.shared.command.GetMapIcons;
import org.activityinfo.shared.command.result.MapIconResult;
import org.junit.Assert;
import org.junit.Test;
/*
 * @author Alex Bertram
 */

public class GetMapIconsTest {

    @Test
    public void testGetMapIcons() throws Exception {

        GetMapIconsHandler handler = new GetMapIconsHandler("war/mapicons");
        MapIconResult result = (MapIconResult) handler.execute(new GetMapIcons(), new User());

        Assert.assertTrue(result.getData().size() > 0);

    }
}
