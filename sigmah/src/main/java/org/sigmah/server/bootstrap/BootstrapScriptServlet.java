/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import com.bedatadriven.rebar.appcache.server.BootstrapServlet;
import com.bedatadriven.rebar.appcache.server.PropertyProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.sigmah.server.domain.Authentication;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Singleton
public class BootstrapScriptServlet extends BootstrapServlet {

    @Inject
    public BootstrapScriptServlet(Provider<EntityManager> entityManager) {
        registerProvider("locale", new LocaleProvider(entityManager));
    }

    private class LocaleProvider implements PropertyProvider {

        private final Provider<EntityManager> entityManager;

        public LocaleProvider(Provider<EntityManager> entityManager) {
            this.entityManager = entityManager;
        }

        @Override
        public String get(HttpServletRequest req) {
            Authentication auth = entityManager.get().find(Authentication.class, getAuthToken(req));
            return auth.getUser().getLocale();
        }

        private String getAuthToken(HttpServletRequest req) {
            for(Cookie cookie : req.getCookies()) {
                if(cookie.getName().equals("authToken")) {
                    return cookie.getValue();
                }
            }
            throw new IllegalStateException("User is not authenticated");
        }
    }
}
