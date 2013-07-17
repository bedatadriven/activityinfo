package org.activityinfo.server.util;

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
        bind(JnlpDownloadServlet.class).in(Singleton.class);
        serve("/webstart/*").with(JnlpDownloadServlet.class);
    }
}
