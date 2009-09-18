package org.activityinfo.client.util;

import com.google.gwt.user.client.Cookies;

import java.util.Date;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CookieProviderImpl implements CookieProvider {

    @Override
    public String getCookie(String name) {
        return Cookies.getCookie(name);
    }

    @Override
    public void setCookie(String name, String value, Date expires) {
       Cookies.setCookie(name, value, expires);
    }

    @Override
    public void removeCookie(String name) {
        Cookies.removeCookie(name);
    }
}
