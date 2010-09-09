package org.sigmah.server.util;

import java.util.Locale;

import org.sigmah.shared.domain.User;

/**
 * Static Locale helper methods.
 *
 * @author Alex Bertram
 */
public class LocaleHelper {

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
