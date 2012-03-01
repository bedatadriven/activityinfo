/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.authentication;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.inject.servlet.ServletModule;

/**
 * Guice Module defining bindings for Authentication interfaces.
 * (The default is checks the password against the database)
 *
 * See http://code.google.com/p/google-web-toolkit-incubator/wiki/LoginSecurityFAQ for background
 */
public class AuthenticationModule extends ServletModule {

	@Override
	protected void configureServlets() {
        bind(Authenticator.class).to(DatabaseAuthenticator.class);
        bind(AuthenticatedUser.class).toProvider(ServerSideAuthProvider.class);
        filter("/*").through(AuthenticationFilter.class);
	}
}
