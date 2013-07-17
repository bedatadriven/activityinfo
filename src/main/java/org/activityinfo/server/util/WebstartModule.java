package org.activityinfo.server.util;

import java.util.HashMap;
import java.util.Map;

import jnlp.sample.servlet.JnlpDownloadServlet;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;


/**
 * Binds the Sun's web start download servlet to serve JNLP apps
 * like geoadmin
 */
public class WebstartModule extends ServletModule {
    @Override
    protected void configureServlets() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("logLevel", "DEBUG");

        bind(JnlpDownloadServlet.class).in(Singleton.class);
        serve("/webstart/*").with(JnlpDownloadServlet.class, params);
    }
}
