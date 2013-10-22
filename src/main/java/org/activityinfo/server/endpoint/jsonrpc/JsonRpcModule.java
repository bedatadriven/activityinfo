package org.activityinfo.server.endpoint.jsonrpc;

import com.google.inject.servlet.ServletModule;

public class JsonRpcModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/command").with(JsonRpcServlet.class);
    }
}
