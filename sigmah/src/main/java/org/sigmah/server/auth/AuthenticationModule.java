/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.auth;

import com.google.inject.AbstractModule;
import org.sigmah.server.auth.impl.DatabaseAuthenticator;

/**
 * Guice Module defining bindings for Authentication interfaces.
 * (The default is checks the password against the database)
 *
 * See http://code.google.com/p/google-web-toolkit-incubator/wiki/LoginSecurityFAQ for background
 */
public class AuthenticationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Authenticator.class).to(DatabaseAuthenticator.class);
    }
}
