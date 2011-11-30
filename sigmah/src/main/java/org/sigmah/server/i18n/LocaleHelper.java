package org.sigmah.server.i18n;

import java.util.Locale;

import org.sigmah.server.database.hibernate.entity.User;

/**
 * Static Locale helper methods.
 *
 * @author Alex Bertram
 */
public class LocaleHelper {

	private LocaleHelper() {}
	
    public static Locale getLocaleObject(User u) {
    	String locale = u.getLocale();
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
