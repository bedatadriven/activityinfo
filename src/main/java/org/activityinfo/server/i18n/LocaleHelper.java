package org.activityinfo.server.i18n;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Locale;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.auth.AuthenticatedUser;

/**
 * Static Locale helper methods.
 * 
 * @author Alex Bertram
 */
public final class LocaleHelper {

    private LocaleHelper() {
    }

    public static Locale getLocaleObject(User u) {
        return getLocaleObject(u.getLocale());
    }

    public static Locale getLocaleObject(AuthenticatedUser user) {
        return getLocaleObject(user.getUserLocale());
    }

    private static Locale getLocaleObject(String locale) {
        if (locale != null) {
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
