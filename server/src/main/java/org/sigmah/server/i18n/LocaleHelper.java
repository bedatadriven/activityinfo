package org.sigmah.server.i18n;

import java.util.Locale;

import org.activityinfo.shared.auth.AuthenticatedUser;
import org.sigmah.server.database.hibernate.entity.User;

/**
 * Static Locale helper methods.
 *
 * @author Alex Bertram
 */
public final class LocaleHelper {

	private LocaleHelper() {}
	
    public static Locale getLocaleObject(User u) {
        return getLocaleObject(u.getLocale());
    }

    public static Locale getLocaleObject(AuthenticatedUser user) {
    	return getLocaleObject(user.getUserLocale());
    }
    
	private static Locale getLocaleObject(String locale) {
		if(locale != null) {
            Locale[] locales = Locale.getAvailableLocales();
            for (Locale l : locales) {
                if (locale.startsWith(l.getLanguage())) {
                    return l;
                }
            }
        }
        return Locale.getDefault();
	}
}
