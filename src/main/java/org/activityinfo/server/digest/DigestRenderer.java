package org.activityinfo.server.digest;

import java.io.IOException;

public interface DigestRenderer {

    public abstract String renderHtml(DigestModel model) throws IOException;

}