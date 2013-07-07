package org.activityinfo.server.util;

import com.google.inject.servlet.ServletModule;
import jnlp.sample.servlet.JnlpDownloadServlet;


/**
 * Binds the Sun's web start download servlet to serve JNLP apps
 * like geoadmin
 */
public class WebstartModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/webstart/*").with(JnlpDownloadServlet.class);
    }
}
