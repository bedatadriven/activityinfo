package org.activityinfo.embed.client;

import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.dto.AnonymousUser;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.inject.Provider;


/**
 * Supplies user <code>Authentication</code> information from
 * the <code>authToken</code> and <code>email</code> only for Anonymous User.
 *
 * @author Muhammad Abid
 */
public class ClientSideAnonymousAuthProvider implements Provider<AuthenticatedUser> {

    public AuthenticatedUser get() {
            return new AuthenticatedUser(AnonymousUser.AUTHTOKEN,AnonymousUser.USER_ID,AnonymousUser.USER_EMAIL,currentLocale());
    }
    
    private String currentLocale() {
    	return LocaleInfo.getCurrentLocale().getLocaleName();
    }
}
