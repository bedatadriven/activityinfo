package org.activityinfo.server.bootstrap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class RestMockUtils {

    static UriInfo mockUriInfo(String uri) throws URISyntaxException {
        UriInfo uriInfo = createMock(UriInfo.class);
        expect(uriInfo.getRequestUri()).andReturn(new URI(uri)).anyTimes();
        expect(uriInfo.getAbsolutePathBuilder()).andReturn(UriBuilder.fromUri(uri));
        replay(uriInfo);
        return uriInfo;
    }

}
