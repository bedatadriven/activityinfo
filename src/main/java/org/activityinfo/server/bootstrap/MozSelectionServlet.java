package org.activityinfo.server.bootstrap;


import javax.servlet.http.HttpServletRequest;

import com.bedatadriven.rebar.appcache.server.DefaultSelectionServlet;
import com.bedatadriven.rebar.appcache.server.PropertyProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MozSelectionServlet extends DefaultSelectionServlet {

	@Inject
    public MozSelectionServlet() {
        registerProvider("locale", new LocaleProvider());
    }

    private class LocaleProvider implements PropertyProvider {
        @Override
        public String get(HttpServletRequest req) {
        	return "en";
        }
    }
}
